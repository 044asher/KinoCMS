package com.CMS.kinoCMS.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 4000, message = "Description should be less than 4000 symbols")
    @NotEmpty(message = "Description should not be empty")
    private String description;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    private String mainImage;

    @Size(max = 255, message = "Link should be less than 255 symbols")
    @NotEmpty(message = "Insert the link, it should not be empty")
    private String link;

    private LocalDate dateOfCreation;

    private boolean notActive;

    @ElementCollection
    @CollectionTable(name = "news_images", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "image_name")
    @Size(max = 5)
    private List<String> images = new ArrayList<>();

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
