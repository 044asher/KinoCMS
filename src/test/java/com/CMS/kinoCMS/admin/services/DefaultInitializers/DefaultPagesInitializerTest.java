package com.CMS.kinoCMS.admin.services.DefaultInitializers;

import com.CMS.kinoCMS.admin.services.Pages.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultPagesInitializerTest {

    @InjectMocks
    private DefaultPagesInitializer defaultPagesInitializer;

    @Mock
    private PageService pageService;

    @Test
    void testInitializeDefaultPages() {
        List<String> defaultPageNames = Arrays.asList(
                "О кинотеатре",
                "Кафе-Бар",
                "Vip-зал",
                "Реклама",
                "Детская комната",
                "Мобильное приложение"
        );

        when(pageService.existsByName(anyString())).thenReturn(false);
        defaultPagesInitializer.initializeDefaultPages();

        for(String pageName : defaultPageNames) {
            verify(pageService).save(argThat(
                    page -> page.getName().equals(pageName)
                    && page.isDefault()
                    && page.isNotActive()
            ));
        }

        for (String pageName : defaultPageNames) {
            verify(pageService).populateDefaultSEOFields(argThat(
                    page -> page.getName().equals(pageName)));
        }

    }
}
