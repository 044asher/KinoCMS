package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.FilmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/films")
public class films {
    private final FileUploadService fileUploadService;
    private final FilmService filmService;

    @Autowired
    public films(FileUploadService fileUploadService, FilmService filmService) {
        this.fileUploadService = fileUploadService;
        this.filmService = filmService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String films(Model model) {
        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);
        return "films/films";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addFilm(Model model) {
        model.addAttribute("film", new Film());
        model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMax"));
        return "films/film-add";
    }

    @PostMapping("/add")
    public String addFilmPost(@Valid Film film,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("filmTypes") List<String> filmTypes,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "films/film-add";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String resultFileName = fileUploadService.uploadFile(file);
                film.setMainImage(resultFileName);
            }
            film.setTypes(String.join(",", filmTypes));
            filmService.save(film);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно добавлен!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/add-film";
        }
        return "redirect:/admin/films";
    }


    @GetMapping("/edit/{id}")
    public String filmEdit(@PathVariable(value = "id") long id, Model model) {
        Optional<Film> optionalFilm = filmService.findById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            model.addAttribute("film", film);
            model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMAX"));
            model.addAttribute("selectedFilmTypes", film.getTypes() != null ? film.getTypes().split(",") : new String[]{});
            return "films/film-edit";
        } else {
            model.addAttribute("message", "Фильм с ID " + id + " не найден.");
            return "redirect:/admin/films";
        }
    }

    @PostMapping("/edit/{id}")
    public String editFilmPost(@PathVariable("id") Long id,
                               @Valid Film film,
                               BindingResult bindingResult,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(value = "filmTypes", required = false) List<String> filmTypes,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "films/film-edit";
        }

        try {
            Film existingFilm = filmService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

            if (file != null && !file.isEmpty()) {
                String resultFileName = fileUploadService.uploadFile(file);
                existingFilm.setMainImage(resultFileName);
            }

            existingFilm.setName(film.getName());
            existingFilm.setDescription(film.getDescription());
            existingFilm.setLink(film.getLink());
            if(film.getDate() != null) {
                existingFilm.setDate(film.getDate());
            }
            existingFilm.setUrlSEO(film.getUrlSEO());
            existingFilm.setTitleSEO(film.getTitleSEO());
            existingFilm.setKeywordsSEO(film.getKeywordsSEO());
            existingFilm.setDescriptionSEO(film.getDescriptionSEO());

            if (filmTypes != null) {
                String typesString = String.join(",", filmTypes);
                existingFilm.setTypes(typesString);
            } else {
                existingFilm.setTypes("");
            }

            filmService.save(existingFilm);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно обновлен!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/admin/films";
        }

        return "redirect:/admin/films";
    }


}
