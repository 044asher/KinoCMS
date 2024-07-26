package com.CMS.kinoCMS.config.ControllerAdvices;

import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import com.CMS.kinoCMS.admin.services.Pages.MainPageService;
import com.CMS.kinoCMS.admin.services.Pages.PageService;
import com.CMS.kinoCMS.config.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final PageService pageService;
    private final MainPageService mainPageService;

    @Autowired
    public GlobalControllerAdvice(PageService pageService, MainPageService mainPageService) {
        this.pageService = pageService;
        this.mainPageService = mainPageService;
    }

    @ModelAttribute
    public void addUserAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails userDetails) {
            model.addAttribute("firstName", userDetails.getFirstName());
            model.addAttribute("lastName", userDetails.getLastName());
            model.addAttribute("role", userDetails.getRole());
            model.addAttribute("userId", userDetails.getId());
        }


    }

    @ModelAttribute
    public void getNonDefaultActivePages(Model model) {
        List<Page> nonDefaultActivePages = pageService.findByIsDefault(false).stream()
                .filter(page -> !page.isNotActive())
                .toList();
        model.addAttribute("nonDefaultActivePages", nonDefaultActivePages);
    }

    @ModelAttribute
    public void getNumbersForNavbar(Model model) {
        MainPage mainPage = mainPageService.findAll().getFirst();
        if(mainPage != null && !mainPage.isNotActive()) {
            model.addAttribute("firstNumber", mainPage.getFirsNumber());
            model.addAttribute("secondNumber", mainPage.getSecondNumber());
        }
    }

}
