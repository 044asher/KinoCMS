package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posters")
public class PostersController {
    private final FilmService filmService;

    @Autowired
    public PostersController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String posters(Model model) {
        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> prePremiereFilms = filmService.getPrePremieresFilms(true);

        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("prePremiereFilms", prePremiereFilms);

        return "users-part/posters/posters";
    }

    @GetMapping("/upcoming")
    public String upcomingFilms(Model model) {
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("upcomingFilms", upcomingFilms);

        return "users-part/posters/upcoming";
    }


}
