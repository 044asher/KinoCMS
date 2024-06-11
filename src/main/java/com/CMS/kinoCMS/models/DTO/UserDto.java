package com.CMS.kinoCMS.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirthday;
    private String gender;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDate dateOfRegistration;
    private Long cityId;
}

