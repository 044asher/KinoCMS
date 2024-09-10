package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.PageRepository;
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

    @Autowired
    public PageService(PageRepository pageRepository, FileUploadService fileUploadService, MenuItemService menuItemService, MainPageService mainPageService, ContactsPageService contactsPageService) {
        this.pageRepository = pageRepository;
        this.fileUploadService = fileUploadService;
        this.menuItemService = menuItemService;
        this.mainPageService = mainPageService;
        this.contactsPageService = contactsPageService;
    }

    public void save(Page page) {
        log.info("Start PageService - save for page: {}", page.getName());
        pageRepository.save(page);
        log.info("Successfully executed PageService - save for page: {}", page.getName());
    }

    public List<Page> findAll() {
        log.info("Start PageService - findAll");
        List<Page> pages = pageRepository.findAll();
        log.info("Successfully executed PageService - findAll");
        return pages;
    }

    public Optional<Page> findById(long id) {
        log.info("Start PageService - findById for id: {}", id);
        Optional<Page> page = pageRepository.findById(id);
        log.info("Successfully executed PageService - findById for id: {}", id);
        return page;
    }

    public List<Page> findByIsDefault(boolean isDefault) {
        log.info("Start PageService - findByIsDefault for isDefault: {}", isDefault);
        List<Page> pages = pageRepository.findAllByIsDefault(isDefault);
        log.info("Successfully executed PageService - findByIsDefault for isDefault: {}", isDefault);
        return pages;
    }

    public Optional<Page> findByName(String name) {
        log.info("Start PageService - findByName for name: {}", name);
        Optional<Page> page = pageRepository.findByName(name);
        log.info("Successfully executed PageService - findByName for name: {}", name);
        return page;
    }

    public void addNewPage(Page newPage, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start PageService - addNewPage for page: {}", newPage);
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                newPage.setMainImage(fileName);
                log.info("Uploaded main image file: {}", fileName);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                newPage.getImages().addAll(newImageNames.stream().limit(5).toList());
                log.info("Uploaded additional image files: {}", newImageNames);
            }

            newPage.setDateOfCreation(LocalDate.now());
            newPage.setNotActive(false);
            newPage.setDefault(false);
            save(newPage);
            log.info("Successfully executed PageService - addNewPage for page: {}", newPage);
        } catch (IOException e) {
            log.error("Failed to execute PageService - addNewPage for page: {}", newPage, e);
            throw e;
        }
    }

    public void updatePage(Long id, Page page, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start PageService - updatePage for id: {}", id);
        try {
            Page existingPage = findById(id).orElseThrow(() -> new RuntimeException("Page Not Found"));
            if (file != null && !file.isEmpty()) {
                String fileName = fileUploadService.uploadFile(file);
                existingPage.setMainImage(fileName);
                log.info("Uploaded new main image file: {}", fileName);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingPage.getImages().clear();
                existingPage.getImages().addAll(newImageNames.stream().limit(5).toList());
                log.info("Uploaded new additional image files: {}", newImageNames);
            }

            existingPage.setName(page.getName());
            existingPage.setDescription(page.getDescription());
            existingPage.setDescriptionSEO(page.getDescriptionSEO());
            existingPage.setTitleSEO(page.getTitleSEO());
            existingPage.setUrlSEO(page.getUrlSEO());
            existingPage.setKeywordsSEO(page.getKeywordsSEO());
            save(existingPage);
            log.info("Successfully executed PageService - updatePage for id: {}", id);
        } catch (IOException e) {
            log.error("Failed to execute PageService - updatePage for id: {}", id, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Page not found for id: {}", id, e);
            throw e;
        }
    }

    public void addMenuItemToPage(Long pageId, String itemName, double price, String ingredients) {
        log.info("Start PageService - addMenuItemToPage for pageId: {}, itemName: {}", pageId, itemName);
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
                log.info("Successfully executed PageService - addMenuItemToPage for itemName: {}", itemName);
            } else {
                log.warn("Page with id '{}' is not a 'Кафе-Бар' page", pageId);
            }
        } else {
            log.error("Page not found for id: {}", pageId);
            throw new RuntimeException("Page Not Found");
        }
    }

    public void changeStatus(Object pageObject) {
        log.info("Start PageService - changeStatus for pageObject: {}", pageObject);

        log.info("Successfully executed PageService - changeStatus for pageObject: {}", pageObject);
    }

    private void saveMainPage(MainPage mainPage) {
        log.info("Start PageService - saveMainPage for mainPage: {}", mainPage);
        mainPageService.save(mainPage);
        log.info("Successfully executed PageService - saveMainPage for mainPage: {}", mainPage);
    }

    private void saveContact(Contact contact) {
        log.info("Start PageService - saveContact for contact: {}", contact);
        contactsPageService.save(contact);
        log.info("Successfully executed PageService - saveContact for contact: {}", contact);
    }

    public Optional<Page> getPageWithCheck(String pageName, Model model) {
        log.info("Start PageService - getPageWithCheck for pageName: {}", pageName);
        Optional<Page> page = findByName(pageName);
        page.ifPresent(value -> model.addAttribute("page", value));
        log.info("Successfully executed PageService - getPageWithCheck for pageName: {}", pageName);
        return page;
    }

    public void pageDeleteById(Long id) {
        log.info("Start PageService - pageDeleteById for id: {}", id);
        Optional<Page> optionalPage = findById(id);
        if (optionalPage.isPresent()) {
            pageRepository.delete(optionalPage.get());
            log.info("Successfully executed PageService - pageDeleteById for id: {}", id);
        } else {
            log.error("Page not found for id: {}", id);
            throw new RuntimeException("Page Not Found");
        }
    }

    public boolean existsByName(String name) {
        return pageRepository.existsByName(name);
    }

    public void populateDefaultSEOFields(Page page) {
        if (page.getUrlSEO() == null || page.getUrlSEO().isEmpty()) {
            page.setUrlSEO("default-url");
        }
        if (page.getTitleSEO() == null || page.getTitleSEO().isEmpty()) {
            page.setTitleSEO("Default Title");
        }
        if (page.getDescriptionSEO() == null || page.getDescriptionSEO().isEmpty()) {
            page.setDescriptionSEO("Default Description");
        }
        if (page.getKeywordsSEO() == null || page.getKeywordsSEO().isEmpty()) {
            page.setKeywordsSEO("default, keywords");
        }
        if (page.getDescription() == null || page.getDescription().isEmpty()) {
            page.setDescription("Default Page Description");
        }
    }
}
