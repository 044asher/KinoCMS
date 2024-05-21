package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.BannersAndSliders;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin/banners-and-sliders")
public class bannersAndSliders {
    @Value("${upload.path}")
    private String uploadPath;
    private final BannersAndSlidersRepository bannersAndSlidersRepository;

    @Autowired
    public bannersAndSliders(BannersAndSlidersRepository bannersAndSlidersRepository) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String bannersAndSlidersPage(Model model){
        model.addAttribute("banners", bannersAndSlidersRepository.findAll());
        return "banners-and-sliders/banners-and-sliders";
    }

    @PostMapping
    public String bannersAndSlidersPost(@RequestParam MultipartFile background) throws IOException {
        BannersAndSliders bannersAndSliders = new BannersAndSliders();
        if (background != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + background.getOriginalFilename();

            background.transferTo(new File(uploadPath + "/" + resultFilename));

            bannersAndSliders.setBackground(resultFilename);
            bannersAndSlidersRepository.save(bannersAndSliders);
        }
        return "redirect:/admin/banners-and-sliders";
    }
}
