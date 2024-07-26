package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.repositories.Pages.MainPageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class MainPageService {
    private final MainPageRepository mainPageRepository;

    @Autowired
    public MainPageService(MainPageRepository mainPageRepository) {
        this.mainPageRepository = mainPageRepository;
    }

    public List<MainPage> findAll() {
        log.info("Fetching all main pages");
        return mainPageRepository.findAll();
    }

    public Optional<MainPage> findById(long id) {
        log.info("Fetching main page with id: {}", id);
        return mainPageRepository.findById(id);
    }

    public void save(MainPage mainPage) {
        log.info("Saving main page: {}", mainPage);
        mainPageRepository.save(mainPage);
    }

    public void updateMainPage(long id, MainPage mainPage) {
        log.info("Updating main page with id: {}", id);
        MainPage existingMainPage = findById(id)
                .orElseThrow(() -> new RuntimeException("Main Page Not Found"));

        existingMainPage.setFirsNumber(mainPage.getFirsNumber());
        existingMainPage.setSecondNumber(mainPage.getSecondNumber());
        existingMainPage.setTextSEO(mainPage.getTextSEO());
        existingMainPage.setDateOfCreation(existingMainPage.getDateOfCreation());
        existingMainPage.setTitleSEO(mainPage.getTitleSEO());
        existingMainPage.setUrlSEO(mainPage.getUrlSEO());
        existingMainPage.setKeywordsSEO(mainPage.getKeywordsSEO());
        existingMainPage.setDescriptionSEO(mainPage.getDescriptionSEO());

        save(existingMainPage);
    }
}
