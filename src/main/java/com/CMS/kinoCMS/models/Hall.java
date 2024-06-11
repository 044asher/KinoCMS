package com.CMS.kinoCMS.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Number of hall should not be empty")
    private int number;

    @Size(max = 4000, message = "Description should be less than 4000 symbols")
    @NotEmpty(message = "Description should not be empty")
    private String description;

    private String scheme;
    private String topBanner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private LocalDate creationDate;

    // ------------- SEO Block
    @Size(max=255, message = "Url should be less than 255 characters")
    @NotEmpty(message = "Url shouldn't be empty")
    private String urlSEO;

    @Size(max=255, message = "Title should be less than 255 characters")
    @NotEmpty(message = "Title shouldn't be empty")
    private String titleSEO;

    @Size(max=255, message = "Keywords should be less than 255 characters")
    @NotEmpty(message = "Keywords shouldn't be empty")
    private String keywordsSEO;

    @Size(max=4000, message = "Description should be less than 4000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String descriptionSEO;
}

