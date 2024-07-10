package com.CMS.kinoCMS.admin.models.Pages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Item name must be less than 255 characters")
    @NotEmpty(message = "Item name shouldn't be empty")
    private String itemName;

    private double price;

    private String ingredients;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;


}
