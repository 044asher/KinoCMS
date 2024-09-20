package com.CMS.kinoCMS.models.Pages;

import jakarta.persistence.*;
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
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "Контакты";

    private LocalDate dateOfCreation;
    private boolean notActive;


    // ------------- SEO Block

    @Size(max = 255, message = "Url should be less than 255 characters")
    @NotEmpty(message = "Url shouldn't be empty")
    private String urlSEO;

    @Size(max = 255, message = "Title should be less than 255 characters")
    @NotEmpty(message = "Title shouldn't be empty")
    private String titleSEO;

    @Size(max = 255, message = "Keywords should be less than 255 characters")
    @NotEmpty(message = "Keywords shouldn't be empty")
    private String keywordsSEO;

    @Size(max = 4000, message = "Description should be less than 4000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String descriptionSEO;

}
