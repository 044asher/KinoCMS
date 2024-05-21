package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.BannersAndSliders;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
public class stats {
    private final BannersAndSlidersRepository bannersAndSlidersRepository;

    @Autowired
    public stats(BannersAndSlidersRepository bannersAndSlidersRepository) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
    }

    @GetMapping
    public String statsPage(Model model){
        model.addAttribute("banner", bannersAndSlidersRepository.findAll());
        return "stats/stats-main";
    }


}
