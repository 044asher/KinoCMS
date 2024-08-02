package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.PageRepository;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PageService {
    private final PageRepository pageRepository;
    private final FileUploadService fileUploadService;
    private final MenuItemService menuItemService;
    private final MainPageService mainPageService;
    private final ContactsPageService contactsPageService;
    private final CinemaService cinemaService;

    @Autowired
    public PageService(PageRepository pageRepository, FileUploadService fileUploadService, MenuItemService menuItemService, MainPageService mainPageService, ContactsPageService contactsPageService, CinemaService cinemaService) {
        this.pageRepository = pageRepository;
        this.fileUploadService = fileUploadService;
        this.menuItemService = menuItemService;
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
        this.cinemaService = cinemaService;
    }

    public void save(Page page) {
        log.info("Saving page: {}", page);
        pageRepository.save(page);
    }

    public List<Page> findAll() {
        log.info("Fetching all pages");
        return pageRepository.findAll();
    }

    public Optional<Page> findById(long id) {
        log.info("Finding page with id: {}", id);
        return pageRepository.findById(id);
    }

    public List<Page> findByIsDefault(boolean isDefault) {
        log.info("Finding pages with isDefault: {}", isDefault);
        return pageRepository.findAllByIsDefault(isDefault);
    }

    public Optional<Page> findByName(String name) {
        log.info("Finding page with name: {}", name);
        return pageRepository.findByName(name);
    }

    public void addNewPage(Page newPage, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Adding new page: {}", newPage);
        if (file != null && !file.isEmpty()) {
            String fileName = fileUploadService.uploadFile(file);
            newPage.setMainImage(fileName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            newPage.getImages().addAll(newImageNames.stream().limit(5).toList());
        }

        newPage.setDateOfCreation(LocalDate.now());
        newPage.setNotActive(false);
        newPage.setDefault(false);
        save(newPage);
    }

    public void updatePage(Long id, Page page, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Updating page with id: {}", id);
        Page existingPage = findById(id).orElseThrow(() -> new RuntimeException("Page Not Found"));
        if (file != null && !file.isEmpty()) {
            existingPage.setMainImage(fileUploadService.uploadFile(file));
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingPage.getImages().clear();
            existingPage.getImages().addAll(newImageNames.stream().limit(5).toList());
        }

        existingPage.setName(page.getName());
        existingPage.setDescription(page.getDescription());
        existingPage.setDescriptionSEO(page.getDescriptionSEO());
        existingPage.setTitleSEO(page.getTitleSEO());
        existingPage.setUrlSEO(page.getUrlSEO());
        existingPage.setKeywordsSEO(page.getKeywordsSEO());
        save(existingPage);
    }

    public void addMenuItemToPage(Long pageId, String itemName, double price, String ingredients) {
        log.info("Adding menu item '{}' to page with id: {}", itemName, pageId);
        Optional<Page> optionalPage = findById(pageId);
        if (optionalPage.isPresent()) {
            Page page = optionalPage.get();
            if ("Кафе-Бар".equals(page.getName())) {
                MenuItem menuItem = new MenuItem();
                menuItem.setItemName(itemName);
                menuItem.setPrice(price);
                menuItem.setPage(page);
                menuItem.setIngredients(ingredients);
                menuItemService.save(menuItem);
                log.info("Menu item '{}' added successfully", itemName);
            } else {
                log.warn("Page with id '{}' is not a 'Кафе-Бар' page", pageId);
            }
        } else {
            log.error("Page with id '{}' not found", pageId);
            throw new RuntimeException("Page Not Found");
        }
    }

    public void changeStatus(Object pageObject) {
        log.info("Changing status for page object: {}", pageObject);
        switch (pageObject) {
            case MainPage mainPage -> {
                mainPage.setNotActive(!mainPage.isNotActive());
                saveMainPage(mainPage);
            }
            case Contact contactPage -> {
                contactPage.setNotActive(!contactPage.isNotActive());
                saveContact(contactPage);
            }
            case Page page -> {
                page.setNotActive(!page.isNotActive());
                save(page);
            }
            case null, default -> throw new IllegalArgumentException("Unsupported page type");
        }
    }

    private void saveMainPage(MainPage mainPage) {
        log.info("Saving main page: {}", mainPage);
        mainPageService.save(mainPage);
    }

    private void saveContact(Contact contact) {
        log.info("Saving contact page: {}", contact);
        contactsPageService.save(contact);
    }

    public Optional<Page> getPageWithCheck(String pageName, Model model) {
        log.info("Fetching page with name: {}", pageName);
        Optional<Page> page = findByName(pageName);
        if (page.isPresent()) {
            model.addAttribute("page", page.get());
            if (page.get().isNotActive()) {
                List<Cinema> cinemasForNotActivePage = cinemaService.findAll().stream().limit(6).toList();
                model.addAttribute("cinemasForNotActivePage", cinemasForNotActivePage);
                log.info("Page '{}' is not active, adding cinemas for display", pageName);
                return Optional.empty();
            }
        }
        return page;
    }

    public void pageDeleteById(Long id){
        Optional<Page> optionalPage = findById(id);
        optionalPage.ifPresent(pageRepository::delete);
        log.info("Page with id '{}' deleted successfully", id);
    }
}
