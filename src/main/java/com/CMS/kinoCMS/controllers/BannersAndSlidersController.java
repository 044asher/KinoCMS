package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.BannersAndSliders;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/banners-and-sliders")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class BannersAndSlidersController {
    private final BannersAndSlidersRepository bannersAndSlidersRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public BannersAndSlidersController(BannersAndSlidersRepository bannersAndSlidersRepository, FileUploadService fileUploadService) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String bannersAndSlidersPage(Model model){
        model.addAttribute("banners", bannersAndSlidersRepository.findAll());
        return "banners-and-sliders/banners-and-sliders";
    }

    @PostMapping
    public String bannersAndSlidersPost(@RequestParam MultipartFile background) throws IOException {
        BannersAndSliders bannersAndSliders = new BannersAndSliders();
        if (background != null && !background.isEmpty()) {
            List<BannersAndSliders> existingBackground = bannersAndSlidersRepository.findAll();
            bannersAndSlidersRepository.deleteAll(existingBackground);

            String resultFilename = fileUploadService.uploadFile(background);

            bannersAndSliders.setBackground(resultFilename);
            bannersAndSlidersRepository.save(bannersAndSliders);
        }
        return "redirect:/admin/banners-and-sliders";
    }



}
