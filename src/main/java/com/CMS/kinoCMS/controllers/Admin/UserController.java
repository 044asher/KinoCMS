package com.CMS.kinoCMS.controllers.Admin;

import com.CMS.kinoCMS.models.City;
import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {
    private final UserService userService;
    private final CityRepository cityRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, CityRepository cityRepository) {
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public String userList(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) {
        try {
            Pageable pageable = PageRequest.of(page, 5);
            Page<User> userPage;

            if (StringUtils.hasText(search)) {
                userPage = userService.searchUsers(search, pageable);
            } else {
                userPage = userService.findAllUsers(pageable);
            }

            List<City> cities = cityRepository.findAll();

            model.addAttribute("users", userPage.getContent());
            model.addAttribute("cities", cities);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userPage.getTotalPages());
            model.addAttribute("search", search);
            return "users/user-list";
        } catch (Exception e) {
            logger.error("UserController - userList: Не удалось получить список пользователей: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/edit/{id}")
    public String userEdit(@PathVariable("id") long id, Model model) {
        try {
            User user = userService.findUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
            model.addAttribute("user", user);
            model.addAttribute("cities", cityRepository.findAll());
            return "users/user-edit";
        } catch (Exception e) {
            logger.error("Failed to load user for editing: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/edit/{id}")
    public String userEdit(@PathVariable("id") long id, @Valid @ModelAttribute User user) {
        try {
            User existingUser = userService.findUserById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            if (user.getDateOfBirthday() != null) {
                existingUser.setDateOfBirthday(user.getDateOfBirthday());
            }

            existingUser.setGender(user.getGender());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setCity(user.getCity());

            userService.saveUser(existingUser);
            return "redirect:/admin/users";
        } catch (Exception e) {
            logger.error("Failed to update user with Id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id){
        try {
            Optional<User> optionalUser = userService.findUserById(id);
            optionalUser.ifPresent(userService::delete);
            return "redirect:/admin/users";
        } catch (Exception e) {
            logger.error("Failed to delete user with Id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/add-city")
    public String userAddCity(@ModelAttribute City city){
        cityRepository.save(city);
        return "redirect:/admin/users";
    }
}
