package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.admin.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {
    private final BannersAndSlidersRepository bannersAndSlidersRepository;
    private final FilmService filmService;

    @Autowired
    public MainController(BannersAndSlidersRepository bannersAndSlidersRepository, FilmService filmService) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
        this.filmService = filmService;
    }

    @GetMapping
    public String mainPage(Model model){
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        model.addAttribute("images", bannersAndSliders.getImages());
        model.addAttribute("newsImages", bannersAndSliders.getNewsImages());

        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);

        model.addAttribute("banner", bannersAndSliders.getBackground());

        return "users-part/main-page/main";
    }
}
