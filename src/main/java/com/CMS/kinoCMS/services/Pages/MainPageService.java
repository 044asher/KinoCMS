package com.CMS.kinoCMS.services.Pages;

import com.CMS.kinoCMS.models.Pages.MainPage;
import com.CMS.kinoCMS.repositories.Pages.MainPageRepository;
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
        log.info("Start MainPageService - findAll");
        List<MainPage> mainPages = mainPageRepository.findAll();
        log.info("Successfully executed MainPageService - findAll");
        return mainPages;
    }

    public Optional<MainPage> findById(long id) {
        log.info("Start MainPageService - findById with id: {}", id);
        Optional<MainPage> mainPage = mainPageRepository.findById(id);
        log.info("Successfully executed MainPageService - findById with id: {}", id);
        return mainPage;
    }

    public void save(MainPage mainPage) {
        log.info("Start MainPageService - save for main page");
        mainPageRepository.save(mainPage);
        log.info("Successfully executed MainPageService - save for main page");
    }

    public void updateMainPage(long id, MainPage mainPage) {
        log.info("Start MainPageService - updateMainPage with id: {}", id);
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
        log.info("Successfully executed MainPageService - updateMainPage with id: {}", id);
    }
}
