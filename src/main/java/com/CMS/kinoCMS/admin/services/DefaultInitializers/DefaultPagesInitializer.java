package com.CMS.kinoCMS.admin.services.DefaultInitializers;

import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.Pages.PageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultPagesInitializer {
    private final PageService pageService;

    @Autowired
    public DefaultPagesInitializer(PageService pageService) {
        this.pageService = pageService;
    }

    @PostConstruct
    public void initializeDefaultPages() {
        List<String> defaultPageNames = Arrays.asList(
                "О кинотеатре",
                "Кафе-Бар",
                "Vip-зал",
                "Реклама",
                "Детская комната",
                "Мобильное приложение"
        );
        for (String pageName : defaultPageNames) {
            if (!pageService.existsByName(pageName)) {
                Page defaultPage = new Page();
                defaultPage.setName(pageName);
                defaultPage.setDefault(true);
                defaultPage.setDateOfCreation(LocalDate.now());
                defaultPage.setNotActive(true);
                pageService.populateDefaultSEOFields(defaultPage);
                pageService.save(defaultPage);
            }
        }
    }
}
