package com.CMS.kinoCMS.admin.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Название шаблона не должно быть пустым")
    private String name;

    @NotEmpty(message = "Тема шаблона не должна быть пустой")
    private String subject;

    @NotEmpty(message = "Содержимое шаблона не должно быть пустым")
    private String content;

}
