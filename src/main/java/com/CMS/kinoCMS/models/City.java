package com.CMS.kinoCMS.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name of city should not be empty")
    private String name;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    private List<User> users;

//    @OneToMany(mappedBy = "city")
//    @JsonManagedReference
//    private List<Cinema> cinemas;
}
