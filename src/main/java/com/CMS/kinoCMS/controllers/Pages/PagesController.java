package com.CMS.kinoCMS.controllers.Pages;

import com.CMS.kinoCMS.models.Pages.Contact;
import com.CMS.kinoCMS.models.Pages.MainPage;
import com.CMS.kinoCMS.models.Pages.Page;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.Pages.ContactsPageService;
import com.CMS.kinoCMS.services.Pages.MainPageService;
import com.CMS.kinoCMS.services.Pages.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin/pages")
public class PagesController {

    private final MainPageService mainPageService;
    private final ContactsPageService contactsPageService;
    private final PageService pageService;
    private final FileUploadService fileUploadService;

    @Autowired
    public PagesController(MainPageService mainPageService, ContactsPageService contactsPageService, PageService pageService, FileUploadService fileUploadService) {
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
        this.pageService = pageService;
        this.fileUploadService = fileUploadService;
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
                          @RequestParam(required = false)MultipartFile file,
                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "pages/pages-add";
        }
        try{
            if(file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                newPage.setMainImage(fileName);
            }
            newPage.setDateOfCreation(LocalDate.now());
            newPage.setNotActive(false);
            pageService.save(newPage);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/admin/pages";
    }


    @PostMapping("/main-page/{id}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable long id) {
        Optional<MainPage> optionalMainPage = mainPageService.findById(id);
        if(optionalMainPage.isPresent()) {
            MainPage mainPage = optionalMainPage.get();
            mainPage.setNotActive(!mainPage.isNotActive());
            mainPageService.save(mainPage);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contacts/{id}/change-status")
    public ResponseEntity<Void> changeStatusContacts(@PathVariable long id) {
        Optional<Contact> optionalContactsPage = contactsPageService.findById(id);
        if(optionalContactsPage.isPresent()) {
            Contact contactPage = optionalContactsPage.get();
            contactPage.setNotActive(!contactPage.isNotActive());
            contactsPageService.save(contactPage);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeStatusPages(@PathVariable long id) {
        Optional<Page> optionalPage = pageService.findById(id);
        if(optionalPage.isPresent()) {
            Page page = optionalPage.get();
            page.setNotActive(!page.isNotActive());
            pageService.save(page);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
