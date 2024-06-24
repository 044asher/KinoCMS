package com.CMS.kinoCMS.services.pages;

import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.repositories.Pages.PageRepository;
import com.CMS.kinoCMS.admin.services.Pages.PageService;
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
public class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @InjectMocks
    private PageService pageService;

    @Test
    public void shouldSavePage() {
        Page page = new Page();
        page.setId(1L);

        pageService.save(page);

        Mockito.verify(pageRepository, Mockito.times(1)).save(page);
    }

    @Test
    public void shouldReturnAllPages() {
        Page page1 = new Page();
        Page page2 = new Page();
        List<Page> pageList = List.of(page1, page2);

        Mockito.when(pageRepository.findAll()).thenReturn(pageList);

        List<Page> result = pageService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(pageList, result);
    }

    @Test
    public void shouldReturnPageById() {
        long id = 1L;
        Page page = new Page();
        page.setId(id);

        Mockito.when(pageRepository.findById(id)).thenReturn(Optional.of(page));

        Optional<Page> result = pageService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(page, result.get());
    }

    @Test
    public void shouldReturnPagesByIsDefault() {
        boolean isDefault = true;
        Page page1 = new Page();
        page1.setDefault(isDefault);
        Page page2 = new Page();
        page2.setDefault(isDefault);
        List<Page> pageList = List.of(page1, page2);

        Mockito.when(pageRepository.findAllByIsDefault(isDefault)).thenReturn(pageList);

        List<Page> result = pageService.findByIsDefault(isDefault);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(pageList, result);
    }
}
