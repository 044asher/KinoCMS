package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Banners.BannerNewsActions;
import com.CMS.kinoCMS.admin.repositories.BannersAndSlidersRepository;
import com.CMS.kinoCMS.admin.models.Banners.BannerImage;
import com.CMS.kinoCMS.admin.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.admin.services.FileUploadService;
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

        List<BannersAndSliders> bannersAndSlidersList = bannersAndSlidersRepository.findAll();
        model.addAttribute("bannersAndSliders", bannersAndSlidersList);

        if (!bannersAndSlidersList.isEmpty()) {
            BannersAndSliders bannersAndSliders = bannersAndSlidersList.get(0);
            model.addAttribute("bannerImages", bannersAndSliders.getImages());
            model.addAttribute("newsImages", bannersAndSliders.getNewsImages());
        }

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
        bannersAndSliders.addBannerImage(image);

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




    @PostMapping("/upload-news-image")
    public String uploadNewsImage(@RequestParam String newsImageUrl, @RequestParam String newsImageCaption) {
        logger.info("Entering uploadNewsImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerNewsActions image = new BannerNewsActions();
        image.setUrl(newsImageUrl);
        image.setCaption(newsImageCaption);
        bannersAndSliders.addNewsImage(image);

        bannersAndSlidersRepository.save(bannersAndSliders);

        logger.info("News Image uploaded and saved successfully");
        logger.info("Exiting uploadNewsImage method");

        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/delete-news-image")
    public String deleteNewsImage(@RequestParam Long imageId) {
        logger.info("Entering deleteNewsImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerNewsActions image = bannersAndSliders.getNewsImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            bannersAndSliders.removeImage(image);
            bannersAndSlidersRepository.save(bannersAndSliders);
            logger.info("News image deleted successfully");
        } else {
            logger.warn("News image with id {} not found", imageId);
        }

        logger.info("Exiting deleteNewsImage method");
        return "redirect:/admin/banners-and-sliders";
    }


    @PostMapping("/edit-news-image")
    public String editNewsImage(@RequestParam Long imageId, @RequestParam String imageUrl, @RequestParam String caption) {
        logger.info("Entering editNewsImage method");

        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().get(0);
        BannerNewsActions image = bannersAndSliders.getNewsImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            image.setUrl(imageUrl);
            image.setCaption(caption);
            bannersAndSlidersRepository.save(bannersAndSliders);
            logger.info("News image edited successfully");
        } else {
            logger.warn("News-image with id {} not found", imageId);
        }

        logger.info("Exiting editNewsImage method");
        return "redirect:/admin/banners-and-sliders";
    }

}
