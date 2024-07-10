package com.CMS.kinoCMS.admin.models.Pages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Name must be less than 255 characters")
    @NotEmpty(message = "Name shouldn't be empty")
    private String name;

    @Size(max = 4000, message = "Description must be less than 5000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String description;

    private String mainImage;

    private LocalDate dateOfCreation;
    private boolean notActive;
    private boolean isDefault;



    @ElementCollection
    @CollectionTable(name = "page_images", joinColumns = @JoinColumn(name = "page_id"))
    @Column(name = "image_name")
    @Size(max = 5)
    private List<String> images = new ArrayList<>();

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



    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();
}
