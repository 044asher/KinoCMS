package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Banners.BannerImage;
import com.CMS.kinoCMS.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.services.FileUploadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/banners-and-sliders")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class BannersAndSlidersController {
    private static final Logger logger = LogManager.getLogger(BannersAndSlidersController.class);

    private final BannersAndSlidersRepository bannersAndSlidersRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public BannersAndSlidersController(BannersAndSlidersRepository bannersAndSlidersRepository, FileUploadService fileUploadService) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public String bannersAndSlidersPage(Model model) {
        logger.info("Entering bannersAndSlidersPage method");
        model.addAttribute("banners", bannersAndSlidersRepository.findAll());
        logger.info("Exiting bannersAndSlidersPage method");
        return "banners-and-sliders/banners-and-sliders";
    }

    @PostMapping("/upload-background")
    public String uploadBackground(@RequestParam MultipartFile background) throws IOException {
        logger.info("Entering uploadBackground method");
        BannersAndSliders bannersAndSliders = new BannersAndSliders();
        if (background != null && !background.isEmpty()) {
            logger.info("Background file is not empty, proceeding with upload");
            List<BannersAndSliders> existingBackground = bannersAndSlidersRepository.findAll();
            bannersAndSlidersRepository.deleteAll(existingBackground);
            logger.info("Deleted existing banners and sliders");

            try {
                String resultFilename = fileUploadService.uploadFile(background);
                bannersAndSliders.setBackground(resultFilename);
                bannersAndSlidersRepository.save(bannersAndSliders);
                logger.info("Background file uploaded and saved successfully with filename: {}", resultFilename);
            } catch (IOException e) {
                logger.error("Error uploading background file", e);
                throw e;
            }
        } else {
            logger.warn("Background file is null or empty");
        }
        logger.info("Exiting uploadBackground method");
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/upload-images")
    public String uploadImages(@RequestParam List<String> imageUrls, @RequestParam List<String> captions) {
        logger.info("Entering uploadImages method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        bannersAndSliders.getImages().clear();

        for (int i = 0; i < imageUrls.size(); i++) {
            BannerImage image = new BannerImage();
            image.setUrl(imageUrls.get(i));
            image.setCaption(captions.get(i));
            bannersAndSliders.addImage(image);
        }

        bannersAndSlidersRepository.save(bannersAndSliders);

        logger.info("Images uploaded and saved successfully");
        logger.info("Exiting uploadImages method");

        return "redirect:/admin/banners-and-sliders";
    }
}
