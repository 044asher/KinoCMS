package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.City;
import com.CMS.kinoCMS.models.DTO.UserMapper;
import com.CMS.kinoCMS.models.DTO.UserResponseDto;
import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.models.DTO.UserDto;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1/user")
public class UserResource {
    private final UserService userService;
    private final CityRepository cityRepository;

    @Autowired
    public UserResource(UserService userService, CityRepository cityRepository) {
        this.userService = userService;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/{userId}")
    public UserResponseDto findById(@PathVariable long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserMapper.toUserResponseDto(user);
    }

    @PostMapping
    public User saveUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDateOfBirthday(userDto.getDateOfBirthday());
        user.setGender(userDto.getGender());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole());
        user.setDateOfRegistration(userDto.getDateOfRegistration());

        City city = cityRepository.findById(userDto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid city Id: " + userDto.getCityId()));
        user.setCity(city);
        
        return userService.saveUser(user);
    }


}
