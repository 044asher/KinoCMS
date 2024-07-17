package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.repositories.CityRepository;
import com.CMS.kinoCMS.admin.models.City;
import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService userService, CityRepository cityRepository) {
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public String userList(Model model){
        try {
            List<User> users = userService.findAllUsers();
            List<City> cities = cityRepository.findAll();

            model.addAttribute("users", users);
            model.addAttribute("cities", cities);

            logger.info("Retrieved user list with {} users", users.size());
            return "users/user-list";
        } catch (Exception e) {
            logger.error("Failed to retrieve user list: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/cities")
    @ResponseBody
    public List<City> getCities(){
        try {
            List<City> cities = cityRepository.findAll();
            logger.info("Retrieved {} cities", cities.size());
            return cities;
        } catch (Exception e) {
            logger.error("Failed to retrieve cities: {}", e.getMessage());
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

            logger.info("Editing user with Id: {}", id);
            return "users/user-edit";
        } catch (Exception e) {
            logger.error("Failed to load user for editing: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/edit/{id}")
    public String userEdit(@PathVariable("id") long id, @ModelAttribute User user) {
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

            logger.info("Updated user with Id: {}", id);
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

            logger.info("Deleted user with Id: {}", id);
            return "redirect:/admin/users";
        } catch (Exception e) {
            logger.error("Failed to delete user with Id {}: {}", id, e.getMessage());
            throw e;
        }
    }

}
