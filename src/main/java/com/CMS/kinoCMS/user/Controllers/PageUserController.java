package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.News;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.NewsService;
import com.CMS.kinoCMS.admin.services.Pages.ContactsPageService;
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
    private final ContactsPageService contactsPageService;

    @Autowired
    public PageUserController(PageService pageService, NewsService newsService, MenuItemService menuItemService, CinemaService cinemaService, ContactsPageService contactsPageService) {
        this.pageService = pageService;
        this.newsService = newsService;
        this.menuItemService = menuItemService;
        this.cinemaService = cinemaService;
        this.contactsPageService = contactsPageService;
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
        List<News> news = newsService.findByNotActive(false);
        if (!news.isEmpty()) {
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
        Optional<Page> cafeBar = pageService.getPageWithCheck("Кафе-Бар", model);
        if (cafeBar.isPresent()) {
            List<MenuItem> menuItems = menuItemService.findAll();
            model.addAttribute("menuItems", menuItems);
            return "users-part/pages/pages";
        }
        return "errors/page-not-active";
    }

    @GetMapping("/vip-hall")
    public String vipHall(Model model) {
        Optional<Page> vipHall = pageService.getPageWithCheck("Vip-зал", model);
        if (vipHall.isPresent()) {
            return "users-part/pages/pages";
        }
        return "errors/page-not-active";

    }

    @GetMapping("/children-room")
    public String childrenRoom(Model model) {
        Optional<Page> childrenRoom = pageService.getPageWithCheck("Детская комната", model);
        if (childrenRoom.isPresent()) {
            return "users-part/pages/pages";
        }
        return "errors/page-not-active";
    }

    @GetMapping("/advertisement")
    public String advertisement(Model model) {
        Optional<Page> advertisement = pageService.getPageWithCheck("Реклама", model);
        if (advertisement.isPresent()) {
            return "users-part/pages/pages";
        }
        return "errors/page-not-active";
    }

    @GetMapping("/mobile-app")
    public String mobileApp(Model model) {
        Optional<Page> mobileApp = pageService.getPageWithCheck("Мобильное приложение", model);
        if (mobileApp.isPresent()) {
            return "users-part/pages/pages";
        }
        return "errors/page-not-active";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        Contact contacts = contactsPageService.findAll().getFirst();
        model.addAttribute("page", contacts);
        if (contacts.isNotActive()) {
            List<Cinema> cinemasForNotActivePage = cinemaService.findAll().stream().limit(6).toList();
            model.addAttribute("cinemasForNotActivePage", cinemasForNotActivePage);
            return "errors/page-not-active";
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

    @GetMapping("/{id}")
    public String getPage(@PathVariable long id, Model model) {
        Optional<Page> page = pageService.findById(id);
        if (page.isPresent() && !page.get().isNotActive()) {
            model.addAttribute("page", page.get());
        }
        return "users-part/pages/pageTemplate";
    }

}
