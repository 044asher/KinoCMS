package com.CMS.kinoCMS.controllers.Public;

import com.CMS.kinoCMS.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
@Controller
@RequestMapping("/")
public class MainController {
    private final BannersAndSlidersRepository bannersAndSlidersRepository;
    private final FilmService filmService;

    @Autowired
    public MainController(BannersAndSlidersRepository bannersAndSlidersRepository, FilmService filmService) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
        this.filmService = filmService;
    }

    @GetMapping
    public String mainPage(Model model) {
        List<BannersAndSliders> bannersAndSlidersList = bannersAndSlidersRepository.findAll();

        if (!bannersAndSlidersList.isEmpty()) {
            BannersAndSliders bannersAndSliders = bannersAndSlidersList.get(0);
            model.addAttribute("images", bannersAndSliders.getImages());
            model.addAttribute("newsImages", bannersAndSliders.getNewsImages());
            model.addAttribute("banner", bannersAndSliders.getBackground());
        } else {
            model.addAttribute("images", Collections.emptyList());
            model.addAttribute("newsImages", Collections.emptyList());
            model.addAttribute("banner", null);
        }

        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);

        return "users-part/main-page/main";
    }

}
