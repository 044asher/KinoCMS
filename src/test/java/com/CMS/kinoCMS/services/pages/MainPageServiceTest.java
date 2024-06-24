package com.CMS.kinoCMS.services.pages;

import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.repositories.Pages.MainPageRepository;
import com.CMS.kinoCMS.admin.services.Pages.MainPageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MainPageServiceTest {

    @Mock
    private MainPageRepository mainPageRepository;

    @InjectMocks
    private MainPageService mainPageService;

    @Test
    public void shouldReturnAllMainPages() {
        MainPage mainPage1 = new MainPage();
        MainPage mainPage2 = new MainPage();
        List<MainPage> mainPageList = List.of(mainPage1, mainPage2);

        Mockito.when(mainPageRepository.findAll()).thenReturn(mainPageList);

        List<MainPage> result = mainPageService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(mainPageList, result);
    }

    @Test
    public void shouldReturnMainPageById() {
        long id = 1L;
        MainPage mainPage = new MainPage();
        mainPage.setId(id);

        Mockito.when(mainPageRepository.findById(id)).thenReturn(Optional.of(mainPage));

        Optional<MainPage> result = mainPageService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mainPage, result.get());
    }

    @Test
    public void shouldSaveMainPage() {
        MainPage mainPage = new MainPage();
        mainPage.setId(1L);

        mainPageService.save(mainPage);

        Mockito.verify(mainPageRepository, Mockito.times(1)).save(mainPage);
    }
}
