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
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        if (background != null && !background.isEmpty()) {
            logger.info("Background file is not empty, proceeding with upload");
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

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam String imageUrl, @RequestParam String caption) {
        logger.info("Entering uploadImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerImage image = new BannerImage();
        image.setUrl(imageUrl);
        image.setCaption(caption);
        bannersAndSliders.addImage(image);

        bannersAndSlidersRepository.save(bannersAndSliders);

        logger.info("Image uploaded and saved successfully");
        logger.info("Exiting uploadImage method");

        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/delete-image")
    public String deleteImage(@RequestParam Long imageId) {
        logger.info("Entering deleteImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerImage image = bannersAndSliders.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            bannersAndSliders.removeImage(image);
            bannersAndSlidersRepository.save(bannersAndSliders);
            logger.info("Image deleted successfully");
        } else {
            logger.warn("Image with id {} not found", imageId);
        }

        logger.info("Exiting deleteImage method");
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/edit-image")
    public String editImage(@RequestParam Long imageId, @RequestParam String imageUrl, @RequestParam String caption) {
        logger.info("Entering editImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerImage image = bannersAndSliders.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            image.setUrl(imageUrl);
            image.setCaption(caption);
            bannersAndSlidersRepository.save(bannersAndSliders);
            logger.info("Image edited successfully");
        } else {
            logger.warn("Image with id {} not found", imageId);
        }

        logger.info("Exiting editImage method");
        return "redirect:/admin/banners-and-sliders";
    }

}