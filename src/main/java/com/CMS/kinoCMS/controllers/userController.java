package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class userController {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Autowired
    public userController(UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "users/user-list";
    }

    @GetMapping("/edit/{id}")
    public String userEdit(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        model.addAttribute("cities", cityRepository.findAll());
        return "users/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String userEditPost(@PathVariable("id") long id, @ModelAttribute User user) {
        User existingUser = userRepository.findById(id)
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

        userRepository.save(existingUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id){
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(userRepository::delete);
        return "redirect:/admin/users";
    }
}
