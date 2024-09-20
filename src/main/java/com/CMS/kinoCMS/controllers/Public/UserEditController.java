package com.CMS.kinoCMS.controllers.Public;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.services.UserService;
import com.CMS.kinoCMS.config.MyUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/user")
public class UserEditController {
    private final UserService userService;
    private final CityRepository cityRepository;

    public UserEditController(UserService userService, CityRepository cityRepository) {
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/edit/{id}")
    public String userEdit(@PathVariable long id, Model model) {
        // Получаем текущего авторизованного пользователя
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername;
        if (principal instanceof UserDetails) { // Если principal объект класса UserDetails
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            throw new AccessDeniedException("Access Denied");
        }

        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

        // Проверяем, что текущий пользователь может редактировать только свои данные
        if (!user.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Access Denied");
        }

        model.addAttribute("user", user);
        model.addAttribute("cities", cityRepository.findAll());
        return "users-part/user/user-edit";
    }

    @PostMapping("/edit")
    public String userEdit(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cities", cityRepository.findAll());
            return "users-part/user/user-edit";
        }

        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.user", "Password does not match");
            model.addAttribute("cities", cityRepository.findAll());
            return "users-part/user/user-edit";
        }

        // Получаем текущего авторизованного пользователя
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername;
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            throw new AccessDeniedException("Access Denied");
        }

        // Получаем существующего пользователя по ID
        User existingUser = userService.findUserById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + user.getId()));

        // Проверяем, что текущий пользователь может редактировать только свои данные
        if (!existingUser.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Access Denied");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setDateOfBirthday(user.getDateOfBirthday());
        existingUser.setGender(user.getGender());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setCity(user.getCity());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        userService.saveUser(existingUser);

        // Обновление аутентификационных данных
        UserDetails updatedUserDetails = new MyUserDetails(existingUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, existingUser.getPassword(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
