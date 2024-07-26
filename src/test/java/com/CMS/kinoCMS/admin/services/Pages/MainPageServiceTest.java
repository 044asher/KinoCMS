package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.repositories.Pages.MainPageRepository;
import com.CMS.kinoCMS.admin.services.Pages.MainPageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainPageServiceTest {

    @Mock
    private MainPageRepository mainPageRepository;

    @InjectMocks
    private MainPageService mainPageService;

    @Test
    void testFindAll() {
        List<MainPage> mainPages = List.of(new MainPage(), new MainPage());
        when(mainPageRepository.findAll()).thenReturn(mainPages);

        List<MainPage> result = mainPageService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mainPageRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        MainPage mainPage = new MainPage();
        when(mainPageRepository.findById(1L)).thenReturn(Optional.of(mainPage));

        Optional<MainPage> result = mainPageService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(mainPage, result.get());
        verify(mainPageRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        MainPage mainPage = new MainPage();

        mainPageService.save(mainPage);

        verify(mainPageRepository, times(1)).save(mainPage);
    }

    @Test
    void testUpdateMainPage() {
        MainPage existingMainPage = new MainPage();
        existingMainPage.setId(1L);

        MainPage updatedMainPage = new MainPage();
        updatedMainPage.setFirsNumber("123");
        updatedMainPage.setSecondNumber("456");
        updatedMainPage.setTextSEO("Updated Text SEO");
        updatedMainPage.setTitleSEO("Updated Title SEO");
        updatedMainPage.setUrlSEO("updated-url-seo");
        updatedMainPage.setKeywordsSEO("updated-keywords-seo");
        updatedMainPage.setDescriptionSEO("updated-description-seo");

        when(mainPageRepository.findById(1L)).thenReturn(Optional.of(existingMainPage));

        mainPageService.updateMainPage(1L, updatedMainPage);

        assertEquals("123", existingMainPage.getFirsNumber());
        assertEquals("456", existingMainPage.getSecondNumber());
        assertEquals("Updated Text SEO", existingMainPage.getTextSEO());
        assertEquals("Updated Title SEO", existingMainPage.getTitleSEO());
        assertEquals("updated-url-seo", existingMainPage.getUrlSEO());
        assertEquals("updated-keywords-seo", existingMainPage.getKeywordsSEO());
        assertEquals("updated-description-seo", existingMainPage.getDescriptionSEO());
        verify(mainPageRepository, times(1)).findById(1L);
        verify(mainPageRepository, times(1)).save(existingMainPage);
    }

    @Test
    void testUpdateMainPage_NotFound() {
        MainPage updatedMainPage = new MainPage();

        when(mainPageRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> mainPageService.updateMainPage(1L, updatedMainPage));

        assertEquals("Main Page Not Found", exception.getMessage());
        verify(mainPageRepository, times(1)).findById(1L);
        verify(mainPageRepository, times(0)).save(any(MainPage.class));
    }
}

