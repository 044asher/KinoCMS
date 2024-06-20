package com.CMS.kinoCMS.models.Pages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 255, message = "Number should be less than 255 symbols")
    @NotEmpty(message = "Number shouldn't be empty")
    private String firsNumber;

    @Size(max = 255, message = "Number should be less than 255 symbols")
    @NotEmpty(message = "Number shouldn't be empty")
    private String secondNumber;

    @Size(max=255, message = "Text must be less than 255 characters")
    @NotEmpty(message = "Text shouldn't be empty")
    private String textSEO;

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
