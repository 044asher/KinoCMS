package com.CMS.kinoCMS.services.Pages;

import com.CMS.kinoCMS.models.Pages.MainPage;
import com.CMS.kinoCMS.repositories.Pages.MainPageRepository;
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

}
