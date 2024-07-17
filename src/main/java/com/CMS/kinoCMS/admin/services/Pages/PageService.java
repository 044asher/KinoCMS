package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.PageRepository;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        pageRepository.save(page);
    }

    public List<Page> findAll() {
       return pageRepository.findAll();
    }

    public Optional<Page> findById(long id) {
        return pageRepository.findById(id);
    }

    public List<Page> findByIsDefault(boolean isDefault) {
        return pageRepository.findAllByIsDefault(isDefault);
    }

    public Optional<Page> findByName(String name) {
        return pageRepository.findByName(name);
    }

    public void addNewPage(Page newPage, MultipartFile file, MultipartFile[] additionalFiles) throws IOException {
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
        Page existingPage = findById(id).orElseThrow(() -> new RuntimeException("Page Not Found"));
        if (file != null && !file.isEmpty()) {
            existingPage.setMainImage(fileUploadService.uploadFile(file));
        }

        if(additionalFiles != null && additionalFiles.length > 0) {
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
            }
        } else {
            throw new RuntimeException("Page Not Found");
        }
    }

    public void changeStatus(Object pageObject) {
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
        mainPageService.save(mainPage);
    }
    private void saveContact(Contact contact) {
        contactsPageService.save(contact);
    }
}
