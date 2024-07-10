package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.services.Pages.ContactsPageService;
import com.CMS.kinoCMS.admin.services.Pages.MainPageService;
import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.admin.models.DTO.Mappers.CinemaMapper;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/pages")
public class PagesController {

    private static final Logger logger = LogManager.getLogger(PagesController.class);

    private final MainPageService mainPageService;
    private final ContactsPageService contactsPageService;
    private final PageService pageService;
    private final FileUploadService fileUploadService;
    private final CinemaService cinemaService;
    private final MenuItemService menuItemService;

    @Autowired
    public PagesController(MainPageService mainPageService, ContactsPageService contactsPageService, PageService pageService, FileUploadService fileUploadService, CinemaService cinemaService, MenuItemService menuItemService) {
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
        this.pageService = pageService;
        this.fileUploadService = fileUploadService;
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
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                newPage.setMainImage(fileName);
                logger.info("File uploaded successfully with name: {}", fileName);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                newPage.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for new page");
            }

            newPage.setDateOfCreation(LocalDate.now());
            newPage.setNotActive(false);
            newPage.setDefault(false);
            pageService.save(newPage);
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

        MainPage existingMainPage = mainPageService.findById(id)
                .orElseThrow(() -> new RuntimeException("Main Page Not Found"));

        existingMainPage.setFirsNumber(mainPage.getFirsNumber());
        existingMainPage.setSecondNumber(mainPage.getSecondNumber());
        existingMainPage.setTextSEO(mainPage.getTextSEO());
        existingMainPage.setDateOfCreation(existingMainPage.getDateOfCreation());
        existingMainPage.setTitleSEO(mainPage.getTitleSEO());
        existingMainPage.setUrlSEO(mainPage.getUrlSEO());
        existingMainPage.setKeywordsSEO(mainPage.getKeywordsSEO());
        existingMainPage.setDescriptionSEO(mainPage.getDescriptionSEO());

        mainPageService.save(existingMainPage);
        logger.info("Main page updated successfully with ID: {}", existingMainPage.getId());
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

        Contact existingContact = contactsPageService.findById(id).orElseThrow(() -> new RuntimeException("Contact Not Found"));
        existingContact.setTitleSEO(contact.getTitleSEO());
        existingContact.setUrlSEO(contact.getUrlSEO());
        existingContact.setKeywordsSEO(contact.getKeywordsSEO());
        existingContact.setDescriptionSEO(contact.getDescriptionSEO());

        contactsPageService.save(existingContact);
        logger.info("Contact page updated successfully with ID: {}", existingContact.getId());
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
            Cinema existingCinema = cinemaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cinema Not Found"));

            if (logoFile != null && !logoFile.isEmpty()) {
                String fileName = fileUploadService.uploadFile(logoFile);
                existingCinema.setLogoName(fileName);
                logger.info("File uploaded successfully with name: {}", fileName);
            }

            existingCinema.setAddress(cinemaUpdateDto.getAddress());
            existingCinema.setXCoordinate(cinemaUpdateDto.getXCoordinate());
            existingCinema.setYCoordinate(cinemaUpdateDto.getYCoordinate());
            cinemaService.save(existingCinema);
            logger.info("Cinema updated successfully with ID: {}", existingCinema.getId());
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
        if(optionalPage.isPresent()) {
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
            Page existingPage = pageService.findById(id).orElseThrow(() -> new RuntimeException("Page Not Found"));
            if (file != null && !file.isEmpty()) {
                existingPage.setMainImage(fileUploadService.uploadFile(file));
                logger.info("File uploaded successfully with name: {}", existingPage.getMainImage());
            }

            if(additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingPage.getImages().clear();
                existingPage.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for page with id: {}", id);
            }

            existingPage.setName(page.getName());
            existingPage.setDescription(page.getDescription());
            existingPage.setDescriptionSEO(page.getDescriptionSEO());
            existingPage.setTitleSEO(page.getTitleSEO());
            existingPage.setUrlSEO(page.getUrlSEO());
            existingPage.setKeywordsSEO(page.getKeywordsSEO());
            pageService.save(existingPage);
            logger.info("Page updated successfully with ID: {}", existingPage.getId());
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
        Optional<Page> optionalPage = pageService.findById(id);
        if (optionalPage.isPresent()) {
            Page page = optionalPage.get();
            if ("Кафе-Бар".equals(page.getName())) {
                MenuItem menuItem = new MenuItem();
                menuItem.setItemName(itemName);
                menuItem.setPrice(price);
                menuItem.setPage(page);
                menuItem.setIngredients(ingredients);
                menuItemService.save(menuItem);
            }
        }
        return "redirect:/admin/pages/" + id;
    }

    @PostMapping("/delete-menu-item/{itemId}")
    public String deleteMenuItem(@PathVariable long pageId, @PathVariable long itemId) {
        menuItemService.deleteById(itemId);
        return "redirect:/admin/pages/" + pageId;
    }

    @PostMapping("/{pageId}/edit-menu-item/{itemId}")
    public String editMenuItem(@PathVariable long pageId, @PathVariable long itemId,
                               @RequestParam String itemName, @RequestParam double price,
                               @RequestParam String ingredients) {
        MenuItem menuItem = menuItemService.findById(itemId);
        if (menuItem != null) {
            menuItem.setItemName(itemName);
            menuItem.setPrice(price);
            menuItem.setIngredients(ingredients);
            menuItemService.save(menuItem);
        }
        return "redirect:/admin/pages/" + pageId;
    }



    @PostMapping("/main-page/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id) {
        logger.info("Entering changeStatus method with ID: {}", id);
        Optional<MainPage> optionalMainPage = mainPageService.findById(id);
        if (optionalMainPage.isPresent()) {
            MainPage mainPage = optionalMainPage.get();
            mainPage.setNotActive(!mainPage.isNotActive());
            mainPageService.save(mainPage);
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
            Contact contactPage = optionalContactsPage.get();
            contactPage.setNotActive(!contactPage.isNotActive());
            contactsPageService.save(contactPage);
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
            Page page = optionalPage.get();
            page.setNotActive(!page.isNotActive());
            pageService.save(page);
            logger.info("Page status changed successfully for ID: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Page with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
