package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.repositories.FilmRepository;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.FilmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/admin/films")
public class films {
    @Value("${upload.path}")
    private String uploadPath;
    private final FilmRepository filmRepository;
    private final FileUploadService fileUploadService;
    private final FilmService filmService;

    @Autowired
    public films(FilmRepository filmRepository, FileUploadService fileUploadService, FilmService filmService) {
        this.filmRepository = filmRepository;
        this.fileUploadService = fileUploadService;
        this.filmService = filmService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String films(Model model){
        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);
        return "films/films";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addFilm(Model model){
        model.addAttribute("film", new Film());
        return "films/film-add";
    }

    @PostMapping("/add")
    public String addFilmPost(@Valid Film film,
                              BindingResult bindingResult,
                              @RequestParam("file")MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "films/film-add";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String resultFileName = fileUploadService.uploadFile(file);
                film.setMainImage(resultFileName);
            }
            filmRepository.save(film);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно добавлен!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/add-film";
        }
        return "redirect:/admin/films";
    }

}
