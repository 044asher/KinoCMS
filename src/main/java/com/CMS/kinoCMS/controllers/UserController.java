package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.City;
import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {
    private final UserService userService;
    private final CityRepository cityRepository;

    @Autowired
    public UserController(UserService userService, CityRepository cityRepository) {
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("cities", cityRepository.findAll());
        return "users/user-list";
    }
    @GetMapping("/cities")
    @ResponseBody
    public List<City> getCities(){
        return cityRepository.findAll();
    }

    @GetMapping("/edit/{id}")
    public String userEdit(@PathVariable("id") long id, Model model) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        model.addAttribute("cities", cityRepository.findAll());
        return "users/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String userEditPost(@PathVariable("id") long id, @ModelAttribute User user) {
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
    }

    @PostMapping("/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id){
        Optional<User> optionalUser = userService.findUserById(id);
        optionalUser.ifPresent(userService::delete);
        return "redirect:/admin/users";
    }
}
