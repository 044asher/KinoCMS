package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FilmService;
import com.CMS.kinoCMS.admin.services.NewsService;
import com.CMS.kinoCMS.admin.services.Pages.MenuItemService;
import com.CMS.kinoCMS.admin.services.Pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pages")
public class PageUserController {
    private final PageService pageService;
    private final NewsService newsService;
    private final MenuItemService menuItemService;
    private final CinemaService cinemaService;

    @Autowired
    public PageUserController(PageService pageService, NewsService newsService, MenuItemService menuItemService, FilmService filmService, CinemaService cinemaService) {
        this.pageService = pageService;
        this.newsService = newsService;
        this.menuItemService = menuItemService;
        this.cinemaService = cinemaService;
    }

    @GetMapping("/about-cinemas")
    public String aboutCinemas(Model model) {
        Optional<Page> aboutCinemaPage = pageService.findByName("О кинотеатре");
        if (aboutCinemaPage.isPresent() && !aboutCinemaPage.get().isNotActive()) {
            model.addAttribute("page", aboutCinemaPage.get());
        }
        return "users-part/pages/about-cinemas";
    }

    @GetMapping("/news")
    public String news(Model model) {
        List<News> news = newsService.findAll();
        if(!news.isEmpty()) {
            model.addAttribute("news", news);
        }
        return "users-part/pages/news";
    }

    @GetMapping("/news/{id}")
    public String newsDetails(@PathVariable long id, Model model) {
        Optional<News> newsPage = newsService.findById(id);
        newsPage.ifPresent(p -> model.addAttribute("news", p));
        return "users-part/pages/news-details";
    }

    @GetMapping("/cafe-bar")
    public String cafeBar(Model model) {
        List<MenuItem> menuItems = menuItemService.findAll();
        Optional<Page> cafeBar = pageService.findByName("Кафе-Бар");
        if (cafeBar.isPresent() && !cafeBar.get().isNotActive()) {
            model.addAttribute("page", cafeBar.get());
            model.addAttribute("menuItems", menuItems);
        }
        return "/users-part/pages/pages";
    }

    @GetMapping("/vip-hall")
    public String vipHall(Model model) {
        Optional<Page> vipHall = pageService.findByName("Vip-зал");
        if(vipHall.isPresent() && !vipHall.get().isNotActive()) {
            model.addAttribute("page", vipHall.get());
        }
        return "users-part/pages/pages";
    }

    @GetMapping("/children-room")
    public String childrenRoom(Model model){
        Optional<Page> childrenRoom = pageService.findByName("Детская комната");
        if(childrenRoom.isPresent() && !childrenRoom.get().isNotActive()) {
            model.addAttribute("page", childrenRoom.get());
        }
        return "users-part/pages/pages";
    }

    @GetMapping("/advertisement")
    public String advertisement(Model model) {
        Optional<Page> advertisement = pageService.findByName("Реклама");
        if(advertisement.isPresent() && !advertisement.get().isNotActive()) {
            model.addAttribute("page", advertisement.get());
        }
        return "users-part/pages/pages";
    }

    @GetMapping("/mobile-app")
    public String mobileApp(Model model) {
        Optional<Page> mobileApp = pageService.findByName("Мобильное приложение");
        if(mobileApp.isPresent() && !mobileApp.get().isNotActive()) {
            model.addAttribute("page", mobileApp.get());
        }
        return "users-part/pages/pages";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        Optional<Page> contacts = pageService.findByName("Контакты");
        if (contacts.isPresent() && !contacts.get().isNotActive()) {
            model.addAttribute("page", contacts.get());
        }

        List<Cinema> allCinemas = cinemaService.findAll();
        List<Cinema> cinemasWithCoordinatesAndAddress = new ArrayList<>();

        for (Cinema cinema : allCinemas) {
            if ((cinema.getXCoordinate() != 0 && cinema.getYCoordinate() != 0) && (cinema.getAddress() != null && !cinema.getAddress().isEmpty())) {
                cinemasWithCoordinatesAndAddress.add(cinema);
            }
        }

        model.addAttribute("cinemas", cinemasWithCoordinatesAndAddress);
        return "users-part/pages/contacts";
    }

}
