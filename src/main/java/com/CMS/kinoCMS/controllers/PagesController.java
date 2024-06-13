package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.models.DTO.Mappers.CinemaMapper;
import com.CMS.kinoCMS.models.Pages.Contact;
import com.CMS.kinoCMS.models.Pages.MainPage;
import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.Pages.ContactsPageService;
import com.CMS.kinoCMS.services.Pages.MainPageService;
import com.CMS.kinoCMS.services.Pages.PageService;
import jakarta.validation.Valid;
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

    private final MainPageService mainPageService;
    private final ContactsPageService contactsPageService;
    private final PageService pageService;
    private final FileUploadService fileUploadService;
    private final CinemaService cinemaService;

    @Autowired
    public PagesController(MainPageService mainPageService, ContactsPageService contactsPageService, PageService pageService, FileUploadService fileUploadService, CinemaService cinemaService) {
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
        this.pageService = pageService;
        this.fileUploadService = fileUploadService;
        this.cinemaService = cinemaService;
    }

    @GetMapping
    public String pages(Model model) {
        model.addAttribute("main", mainPageService.findAll());
        model.addAttribute("contacts", contactsPageService.findAll());
        model.addAttribute("pages", pageService.findAll());
        return "pages/pages-list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("newPage", new Page());
        return "pages/pages-add";
    }

    @PostMapping("/add")
    public String addPage(@ModelAttribute Page newPage,
                          @RequestParam(required = false) MultipartFile file,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/pages-add";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                newPage.setMainImage(fileName);
            }
            newPage.setDateOfCreation(LocalDate.now());
            newPage.setNotActive(false);
            pageService.save(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/pages";
    }

    @GetMapping("/main-page/{id}")
    public String mainPageEdit(@PathVariable int id, Model model) {
        Optional<MainPage> mainPage = mainPageService.findById(id);
        if (mainPage.isPresent()) {
            model.addAttribute("main", mainPage.get());
            return "pages/main-page";
        } else return "redirect:/admin/pages";
    }

    @PostMapping("/main-page/{id}")
    public String mainPageEdit(@PathVariable long id,
                               @Valid @ModelAttribute MainPage mainPage,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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

        return "redirect:/admin/pages";
    }


    @GetMapping("/contacts/{id}")
    public String contactsPageEdit(@PathVariable Long id, Model model) {
        Optional<Contact> contact = contactsPageService.findById(id);
        contact.ifPresent(value -> model.addAttribute("contact", value));

        List<Cinema> cinemas = cinemaService.findAll();
        model.addAttribute("cinemas", cinemas);

        return "pages/contacts-page";
    }

    @PostMapping("/contacts/{id}")
    public String contactsPageEdit(@PathVariable Long id, @Valid @ModelAttribute Contact contact, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
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

        return "redirect:/admin/pages";
    }


    @GetMapping("/contacts/cinema/edit/{id}")
    public String cinemaPageEdit(@PathVariable Long id, Model model) {
        Cinema cinema = cinemaService.findById(id).orElseThrow(() -> new RuntimeException("Cinema Not Found"));
        CinemaUpdateDto cinemaUpdateDto = CinemaMapper.toCinemaUpdateDto(cinema);
        model.addAttribute("cinemaUpdateDto", cinemaUpdateDto);
        model.addAttribute("cinema", cinema);
        return "pages/cinema-page";
    }


    @PostMapping("/contacts/cinema/edit/{id}")
    public String cinemaPageEdit(@PathVariable Long id,
                                 @Valid @ModelAttribute CinemaUpdateDto cinemaUpdateDto,
                                 BindingResult bindingResult,
                                 @RequestParam(required = false) MultipartFile logoFile) {
        if (bindingResult.hasErrors()) {
            return "pages/cinema-page";
        }

        try {
            Cinema existingCinema = cinemaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cinema Not Found"));

            if (logoFile != null && !logoFile.isEmpty()) {
                String fileName = fileUploadService.uploadFile(logoFile);
                existingCinema.setLogoName(fileName);
            }

            existingCinema.setAddress(cinemaUpdateDto.getAddress());
            existingCinema.setXCoordinate(cinemaUpdateDto.getXCoordinate());
            existingCinema.setYCoordinate(cinemaUpdateDto.getYCoordinate());

            cinemaService.save(existingCinema);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/pages/contacts/1";
    }



    @GetMapping("/{id}")
    public String pageEdit(@PathVariable int id, Model model) {
        model.addAttribute("page", pageService.findById(id));
        return "pages/pages-edit";
    }

    @PostMapping("/{id}")
    public String pageEdit(@PathVariable long id,
                           @Valid @ModelAttribute Page page,
                           @RequestParam(required = false) MultipartFile file,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/pages-edit";
        }

        return "";
    }


    @PostMapping("/main-page/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id) {
        Optional<MainPage> optionalMainPage = mainPageService.findById(id);
        if (optionalMainPage.isPresent()) {
            MainPage mainPage = optionalMainPage.get();
            mainPage.setNotActive(!mainPage.isNotActive());
            mainPageService.save(mainPage);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contacts/{id}/change-status")
    public ResponseEntity<Void> changeStatusContacts(@PathVariable long id) {
        Optional<Contact> optionalContactsPage = contactsPageService.findById(id);
        if (optionalContactsPage.isPresent()) {
            Contact contactPage = optionalContactsPage.get();
            contactPage.setNotActive(!contactPage.isNotActive());
            contactsPageService.save(contactPage);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatusPages(@PathVariable long id) {
        Optional<Page> optionalPage = pageService.findById(id);
        if (optionalPage.isPresent()) {
            Page page = optionalPage.get();
            page.setNotActive(!page.isNotActive());
            pageService.save(page);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
