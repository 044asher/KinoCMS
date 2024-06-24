package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.repositories.UserRepository;
import com.CMS.kinoCMS.admin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class registration {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public registration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        if(model.containsAttribute("message")){
            model.addAttribute("message", model.asMap().get("message"));
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Пользователь уже существует!");
            return "redirect:/registration";
        }
        user.setDateOfRegistration(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN");
        user.setGender("prefer_not_to_say");
        userRepository.save(user);

        return "redirect:/login";
    }



}
