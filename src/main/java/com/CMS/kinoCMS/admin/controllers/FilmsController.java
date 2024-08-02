package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.services.FilmService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Controller
@RequestMapping("/admin/films")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class FilmsController {

    private final FilmService filmService;

    @Autowired
    public FilmsController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String films(Model model) {
        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);
        return "films/films";
    }

    @GetMapping("/add")
    public String addFilm(Model model) {
        model.addAttribute("film", new Film());
        model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMax"));
        return "films/film-add";
    }

    @PostMapping("/add")
    public String addFilmPost(@Valid Film film,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile mainFile,
                              @RequestParam("additionalFiles") MultipartFile[] additionalFiles,
                              @RequestParam(value = "filmTypes", required = false) List<String> filmTypes,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) String country,
                              @RequestParam(required = false) String musician,
                              @RequestParam(required = false) List<String> producer,
                              @RequestParam(required = false) String director,
                              @RequestParam(required = false) List<String> writer,
                              @RequestParam(required = false) List<String> genre,
                              @RequestParam(required = false) Integer age,
                              @RequestParam(required = false) Integer time,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in addFilmPost: {}", bindingResult.getAllErrors());
            return "films/film-add";
        }
        try {
            filmService.processFilmData(film, mainFile, additionalFiles, filmTypes, year, country, musician,
                    producer, director, writer, genre, age, time);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно добавлен!");
        } catch (IOException e) {
            log.error("Error uploading file: ", e);
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/admin/films/add";
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
            log.warn("Film with ID {} not found", id);
            model.addAttribute("message", "Фильм с ID " + id + " не найден.");
            return "redirect:/admin/films";
        }
    }

    @PostMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id,
                           @Valid Film film,
                           BindingResult bindingResult,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "additionalFiles", required = false) MultipartFile[] additionalFiles,
                           @RequestParam(value = "filmTypes", required = false) List<String> filmTypes,
                           @RequestParam(required = false) Integer year,
                           @RequestParam(required = false) String country,
                           @RequestParam(required = false) String musician,
                           @RequestParam(required = false) List<String> producer,
                           @RequestParam(required = false) String director,
                           @RequestParam(required = false) List<String> writer,
                           @RequestParam(required = false) List<String> genre,
                           @RequestParam(required = false) Integer age,
                           @RequestParam(required = false) Integer time,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in editFilm: {}", bindingResult.getAllErrors());
            return "films/film-edit";
        }
        try {
            filmService.updateFilm(id, film, file, additionalFiles, filmTypes, year, country, musician,
                    producer, director, writer, genre, age, time);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно обновлен!");
        } catch (IOException e) {
            log.error("Error updating file: ", e);
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Error updating film: ", e);
            redirectAttributes.addFlashAttribute("message", "Ошибка обновления фильма: " + e.getMessage());
        }
        return "redirect:/admin/films";
    }
}
