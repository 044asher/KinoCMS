package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.repositories.Pages.MainPageRepository;
import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainPageService {
    private final MainPageRepository mainPageRepository;

    @Autowired
    public MainPageService(MainPageRepository mainPageRepository) {
        this.mainPageRepository = mainPageRepository;
    }

    public List<MainPage> findAll() {
        return mainPageRepository.findAll();
    }

    public Optional<MainPage> findById(long id) {
        return mainPageRepository.findById(id);
    }

    public void save(MainPage mainPage) {
        mainPageRepository.save(mainPage);
    }

    public void updateMainPage(long id, MainPage mainPage) {
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
