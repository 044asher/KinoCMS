package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.admin.models.DTO.Mappers.CinemaMapper;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.Pages.ContactsPageService;
import com.CMS.kinoCMS.admin.services.Pages.MainPageService;
import com.CMS.kinoCMS.admin.services.Pages.MenuItemService;
import com.CMS.kinoCMS.admin.services.Pages.PageService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/pages")
public class PagesController {

    private static final Logger logger = LogManager.getLogger(PagesController.class);

    private final MainPageService mainPageService;
    private final ContactsPageService contactsPageService;
    private final PageService pageService;
    private final CinemaService cinemaService;
    private final MenuItemService menuItemService;

    @Autowired
    public PagesController(MainPageService mainPageService, ContactsPageService contactsPageService, PageService pageService, CinemaService cinemaService, MenuItemService menuItemService) {
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
        this.pageService = pageService;
        this.cinemaService = cinemaService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String pages(Model model) {
        logger.info("Entering pages method");
        model.addAttribute("main", mainPageService.findAll());
        model.addAttribute("contacts", contactsPageService.findAll());
        model.addAttribute("pages", pageService.findByIsDefault(true));
        model.addAttribute("newPages", pageService.findByIsDefault(false));
        logger.info("Exiting pages method");
        return "pages/pages-list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        logger.info("Entering addPage (GET) method");
        model.addAttribute("newPage", new Page());
        logger.info("Exiting addPage (GET) method");
        return "pages/pages-add";
    }

    @PostMapping("/add")
    public String addPage(@ModelAttribute Page newPage,
                          @RequestParam(required = false) MultipartFile file,
                          BindingResult bindingResult,
                          @RequestParam(value = "additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Entering addPage (POST) method");
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "pages/pages-add";
        }
        try {
            pageService.addNewPage(newPage, file, additionalFiles);
            logger.info("Page saved successfully with ID: {}", newPage.getId());
        } catch (IOException e) {
            logger.error("Error uploading file", e);
        }
        logger.info("Exiting addPage (POST) method");
        return "redirect:/admin/pages";
    }

    @GetMapping("/main-page/{id}")
    public String mainPageEdit(@PathVariable int id, Model model) {
        logger.info("Entering mainPageEdit (GET) method with ID: {}", id);
        Optional<MainPage> mainPage = mainPageService.findById(id);
        if (mainPage.isPresent()) {
            model.addAttribute("main", mainPage.get());
            logger.info("Exiting mainPageEdit (GET) method with mainPage page found");
            return "pages/main-page";
        } else {
            logger.warn("Main page with ID: {} not found", id);
            logger.info("Exiting mainPageEdit (GET) method with redirect");
            return "redirect:/admin/pages";
        }
    }

    @PostMapping("/main-page/{id}")
    public String mainPageEdit(@PathVariable long id,
                               @Valid @ModelAttribute MainPage mainPage,
                               BindingResult bindingResult) {
        logger.info("Entering mainPageEdit (POST) method with ID: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "pages/main-page";
        }

        mainPageService.updateMainPage(id, mainPage);
        logger.info("Main page updated successfully with ID: {}", id);
        logger.info("Exiting mainPageEdit (POST) method");
        return "redirect:/admin/pages";
    }

    @GetMapping("/contacts/{id}")
    public String contactsPageEdit(@PathVariable Long id, Model model) {
        logger.info("Entering contactsPageEdit (GET) method with ID: {}", id);
        Optional<Contact> contact = contactsPageService.findById(id);
        contact.ifPresent(value -> model.addAttribute("contact", value));
        List<Cinema> cinemas = cinemaService.findAll();
        model.addAttribute("cinemas", cinemas);
        logger.info("Exiting contactsPageEdit (GET) method");
        return "pages/contacts-page";
    }

    @PostMapping("/contacts/{id}")
    public String contactsPageEdit(@PathVariable Long id, @Valid @ModelAttribute Contact contact, BindingResult bindingResult, Model model) {
        logger.info("Entering contactsPageEdit (POST) method with ID: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            List<Cinema> cinemas = cinemaService.findAll();
            model.addAttribute("cinemas", cinemas);
            return "pages/contacts-page";
        }

        contactsPageService.updateContactPage(id, contact);
        logger.info("Contact page updated successfully with ID: {}", id);
        logger.info("Exiting contactsPageEdit (POST) method");
        return "redirect:/admin/pages";
    }


    @GetMapping("/contacts/cinema/edit/{id}")
    public String cinemaPageEdit(@PathVariable Long id, Model model) {
        logger.info("Entering cinemaPageEdit (GET) method with ID: {}", id);
        Cinema cinema = cinemaService.findById(id).orElseThrow(() -> new RuntimeException("Cinema Not Found"));
        CinemaUpdateDto cinemaUpdateDto = CinemaMapper.toCinemaUpdateDto(cinema);
        model.addAttribute("cinemaUpdateDto", cinemaUpdateDto);
        model.addAttribute("cinema", cinema);
        logger.info("Exiting cinemaPageEdit (GET) method");
        return "pages/cinema-page";
    }

    @PostMapping("/contacts/cinema/edit/{id}")
    public String cinemaPageEdit(@PathVariable Long id,
                                 @Valid @ModelAttribute CinemaUpdateDto cinemaUpdateDto,
                                 BindingResult bindingResult,
                                 @RequestParam(required = false) MultipartFile logoFile) {
        logger.info("Entering cinemaPageEdit (POST) method with ID: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "pages/cinema-page";
        }

        try {
            contactsPageService.updateCinemaPage(id, cinemaUpdateDto, logoFile);
            logger.info("Cinema updated successfully with ID: {}", id);
        } catch (IOException e) {
            logger.error("Error uploading file", e);
        }
        logger.info("Exiting cinemaPageEdit (POST) method");
        return "redirect:/admin/pages/contacts/1";
    }

    @GetMapping("/{id}")
    public String pageEdit(@PathVariable long id, Model model) {
        logger.info("Entering pageEdit (GET) method with ID: {}", id);
        Optional<Page> optionalPage = pageService.findById(id);
        if (optionalPage.isPresent()) {
            Page page = optionalPage.get();
            model.addAttribute("page", page);

            if ("Кафе-Бар".equals(page.getName())) {
                List<MenuItem> menuItems = menuItemService.findByPage(page);
                model.addAttribute("menuItems", menuItems);
                logger.info("Updating cafe-bar page");
            }

            logger.info("Exiting pageEdit (GET) method with page found");
            return "pages/pages-edit";
        } else {
            logger.warn("Page with ID: {} not found", id);
            logger.info("Exiting pageEdit (GET) method with redirect");
            return "redirect:/admin/pages";
        }
    }

    @PostMapping("/{id}")
    public String pageEdit(@PathVariable long id,
                           @Valid @ModelAttribute Page page,
                           @RequestParam(required = false) MultipartFile file,
                           BindingResult bindingResult,
                           @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Entering pageEdit (POST) method with ID: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "pages/pages-edit";
        }
        try {
            pageService.updatePage(id, page, file, additionalFiles);
            logger.info("Page updated successfully with ID: {}", id);
        } catch (IOException e) {
            logger.error("Error uploading file", e);
        }
        logger.info("Exiting pageEdit (POST) method");
        return "redirect:/admin/pages";
    }


    @PostMapping("/{id}/add-menu-item")
    public String addMenuItem(@PathVariable long id,
                              @RequestParam String itemName,
                              @RequestParam double price,
                              @RequestParam String ingredients) {
        pageService.addMenuItemToPage(id, itemName, price, ingredients);
        return "redirect:/admin/pages/" + id;
    }

    @PostMapping("/main-page/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id) {
        logger.info("Entering changeStatus method with ID: {}", id);
        Optional<MainPage> optionalMainPage = mainPageService.findById(id);
        if (optionalMainPage.isPresent()) {
            pageService.changeStatus(optionalMainPage.get());
            logger.info("Main page status changed successfully for ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Main page with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contacts/{id}/change-status")
    public ResponseEntity<Void> changeStatusContacts(@PathVariable long id) {
        logger.info("Entering changeStatusContacts method with ID: {}", id);
        Optional<Contact> optionalContactsPage = contactsPageService.findById(id);
        if (optionalContactsPage.isPresent()) {
            pageService.changeStatus(optionalContactsPage.get());
            logger.info("Contact page status changed successfully for ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Contact page with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatusPages(@PathVariable long id) {
        logger.info("Entering changeStatusPages method with ID: {}", id);
        Optional<Page> optionalPage = pageService.findById(id);
        if (optionalPage.isPresent()) {
            pageService.changeStatus(optionalPage.get());
            logger.info("Page status changed successfully for ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Page with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
