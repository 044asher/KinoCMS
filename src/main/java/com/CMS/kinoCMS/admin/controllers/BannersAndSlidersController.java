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
        List<BannersAndSliders> bannersAndSlidersList = bannersAndSlidersRepository.findAll();
        model.addAttribute("bannersAndSliders", bannersAndSlidersList);

        if (!bannersAndSlidersList.isEmpty()) {
            BannersAndSliders bannersAndSliders = bannersAndSlidersList.getFirst();
            model.addAttribute("bannerImages", bannersAndSliders.getImages());
            model.addAttribute("newsImages", bannersAndSliders.getNewsImages());
        }
        return "banners-and-sliders/banners-and-sliders";
    }

    @PostMapping("/upload-background")
    public String uploadBackground(@RequestParam MultipartFile background) throws IOException {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        if (background != null && !background.isEmpty()) {
            try {
                String resultFilename = fileUploadService.uploadFile(background);
                bannersAndSliders.setBackground(resultFilename);
                bannersAndSlidersRepository.save(bannersAndSliders);
            } catch (IOException e) {
                logger.error("Error uploading background file", e);
                throw e;
            }
        } else {
            logger.warn("Background file is null or empty");
        }
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam(required = false) MultipartFile imageFile,
                              @RequestParam(required = false) String imageUrl,
                              @RequestParam String caption) throws IOException {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerImage image = new BannerImage();
        image.setCaption(caption);

        if (imageFile != null && !imageFile.isEmpty()) {
            String resultFilename = fileUploadService.uploadFile(imageFile);
            image.setUrl(resultFilename);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            image.setUrl(imageUrl);
        }

        bannersAndSliders.addBannerImage(image);
        bannersAndSlidersRepository.save(bannersAndSliders);

        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/delete-image")
    public String deleteImage(@RequestParam Long imageId) {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerImage image = bannersAndSliders.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            bannersAndSliders.removeImage(image);
            bannersAndSlidersRepository.save(bannersAndSliders);
        } else {
            logger.warn("Image with id {} not found", imageId);
        }
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/edit-image")
    public String editImage(@RequestParam Long imageId, @RequestParam String imageUrl, @RequestParam String caption) {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerImage image = bannersAndSliders.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            image.setUrl(imageUrl);
            image.setCaption(caption);
            bannersAndSlidersRepository.save(bannersAndSliders);
        } else {
            logger.warn("Image with id {} not found", imageId);
        }
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/upload-news-image")
    public String uploadNewsImage(@RequestParam(required = false) MultipartFile newsImageFile,
                                  @RequestParam(required = false) String newsImageUrl,
                                  @RequestParam String newsImageCaption) throws IOException {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerNewsActions image = new BannerNewsActions();
        image.setCaption(newsImageCaption);

        if (newsImageFile != null && !newsImageFile.isEmpty()) {
            String resultFilename = fileUploadService.uploadFile(newsImageFile);
            image.setUrl(resultFilename);
        } else if (newsImageUrl != null && !newsImageUrl.isEmpty()) {
            image.setUrl(newsImageUrl);
        }

        bannersAndSliders.addNewsImage(image);
        bannersAndSlidersRepository.save(bannersAndSliders);

        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/delete-news-image")
    public String deleteNewsImage(@RequestParam Long imageId) {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerNewsActions image = bannersAndSliders.getNewsImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            bannersAndSliders.removeImage(image);
            bannersAndSlidersRepository.save(bannersAndSliders);
        } else {
            logger.warn("News image with id {} not found", imageId);
        }
        return "redirect:/admin/banners-and-sliders";
    }

    @PostMapping("/edit-news-image")
    public String editNewsImage(@RequestParam Long imageId, @RequestParam String imageUrl, @RequestParam String caption) {
        BannersAndSliders bannersAndSliders = bannersAndSlidersRepository.findAll().getFirst();
        BannerNewsActions image = bannersAndSliders.getNewsImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (image != null) {
            image.setUrl(imageUrl);
            image.setCaption(caption);
            bannersAndSlidersRepository.save(bannersAndSliders);
        } else {
            logger.warn("News-image with id {} not found", imageId);
        }
        return "redirect:/admin/banners-and-sliders";
    }
}
