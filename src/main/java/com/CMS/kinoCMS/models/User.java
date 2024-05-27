package com.CMS.kinoCMS.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;
    private String password;
    @Transient
    private String passwordConfirm;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email must be valid")
    private String email;
    private String address;
    private String phoneNumber;

    private String role;

    private LocalDate dateOfBirthday;
    private String gender;

    private LocalDate dateOfRegistration;
    @Size(max = 30, message = "Name should not be more than 30 symbols")
    private String firstName;
    @Size(max = 30, message = "Last name should not be more than 30 symbols")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;


}
