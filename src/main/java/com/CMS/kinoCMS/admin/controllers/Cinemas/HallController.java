package com.CMS.kinoCMS.admin.controllers.Cinemas;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import com.CMS.kinoCMS.admin.services.HallService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cinemas/halls")
public class HallController {
    private static final Logger logger = LoggerFactory.getLogger(HallController.class);

    private final HallService hallService;
    private final FileUploadService fileUploadService;
    private final CinemaService cinemaService;

    @Autowired
    public HallController(HallService hallService, FileUploadService fileUploadService, CinemaService cinemaService) {
        this.hallService = hallService;
        this.fileUploadService = fileUploadService;
        this.cinemaService = cinemaService;
    }

    @GetMapping("/edit/{id}")
    public String hallEdit(@PathVariable long id, Model model) {
        logger.info("Entering hallEdit method with ID: {}", id);
        Optional<Hall> hall = hallService.findById(id);
        hall.ifPresent(value -> model.addAttribute("hall", value));
        logger.info("Exiting hallEdit method");
        return "cinemas/halls/hallEdit";
    }

    @PostMapping("/edit/{id}")
    public String hallSubmit(@PathVariable long id,
                             @Valid @ModelAttribute("hall") Hall hall,
                             @RequestParam(required = false) MultipartFile logo,
                             @RequestParam(required = false) MultipartFile banner,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles,
                             RedirectAttributes redirectAttributes) {
        logger.info("Entering hallSubmit method with ID: {}", id);

        try {
            Hall existingHall = hallService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Hall with id " + id + " not found"));

            existingHall.setNumber(hall.getNumber());
            existingHall.setDescription(hall.getDescription());

            if (logo != null && !logo.isEmpty()) {
                String resultLogoName = fileUploadService.uploadFile(logo);
                existingHall.setScheme(resultLogoName);
                logger.info("Uploaded new logo for cinema, ID: {}", id);
            }

            if (banner != null && !banner.isEmpty()) {
                String resultBannerName = fileUploadService.uploadFile(banner);
                existingHall.setTopBanner(resultBannerName);
                logger.info("Uploaded new banner for cinema, ID: {}", id);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingHall.getImages().clear();
                existingHall.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for hall with id: {}", id);
            }

            existingHall.setUrlSEO(hall.getUrlSEO());
            existingHall.setDescriptionSEO(hall.getDescriptionSEO());
            existingHall.setKeywordsSEO(hall.getKeywordsSEO());
            existingHall.setTitleSEO(hall.getTitleSEO());

            hallService.save(existingHall);
            logger.info("Saved hall with ID: {}", id);

        } catch (IOException e) {
            logger.error("Error while saving hall with ID: {}: {}", id, e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error while saving hall: " + e.getMessage());
            return "redirect:/admin/cinemas/halls/edit/" + id;
        }

        logger.info("Exiting hallSubmit method");
        return "redirect:/admin/cinemas";
    }


    @GetMapping("/add/{cinemaId}")
    public String hallAdd(@PathVariable long cinemaId, Model model){
        logger.info("Entering hallAdd (GET) method for cinemaId: {}", cinemaId);
        Optional<Cinema> cinema = cinemaService.findById(cinemaId);
        if (cinema.isPresent()) {
            model.addAttribute("cinema", cinema.get());
            model.addAttribute("hall", new Hall());
            logger.info("Cinema found for cinemaId: {}", cinemaId);
        } else {
            logger.warn("Cinema not found for cinemaId: {}", cinemaId);
        }
        logger.info("Exiting hallAdd (GET) method");
        return "cinemas/halls/hallAdd";
    }

    @PostMapping("/add/{cinemaId}")
    public String hallAdd(@PathVariable long cinemaId, @Valid @ModelAttribute Hall hall,
                          @RequestParam("logo") MultipartFile logo,
                          @RequestParam("banner") MultipartFile banner,
                          @RequestParam("additionalFiles") MultipartFile[] additionalFiles,
                          RedirectAttributes redirectAttributes) {
        logger.info("Entering hallAdd (POST) method for cinemaId: {}", cinemaId);

        try {
            Optional<Cinema> cinema = cinemaService.findById(cinemaId);
            if (cinema.isPresent()) {
                if (!logo.isEmpty()) {
                    String resultLogoName = fileUploadService.uploadFile(logo);
                    hall.setScheme(resultLogoName);
                    logger.info("Uploaded hall scheme: {}", resultLogoName);
                }
                if (!banner.isEmpty()) {
                    String resultBannerName = fileUploadService.uploadFile(banner);
                    hall.setTopBanner(resultBannerName);
                    logger.info("Uploaded hall banner: {}", resultBannerName);
                }

                if (additionalFiles != null && additionalFiles.length > 0) {
                    List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                    hall.getImages().addAll(newImageNames.stream().limit(5).toList());
                }


                hall.setCinema(cinema.get());
                hall.setCreationDate(LocalDate.now());
                hallService.save(hall);
                logger.info("Saved new hall for cinemaId: {}", cinemaId);
                redirectAttributes.addFlashAttribute("successMessage", "Зал успешно добавлен");
            } else {
                logger.warn("Cinema not found for cinemaId: {}", cinemaId);
                redirectAttributes.addFlashAttribute("errorMessage", "Кинотеатр не найден");
                return "redirect:/admin/cinemas";
            }
        } catch (IOException e) {
            logger.error("Ошибка при добавлении зала для cinemaId: {}: {}", cinemaId, e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении зала: " + e.getMessage());
        }

        logger.info("Exiting hallAdd (POST) method");
        return "redirect:/admin/cinemas/edit/" + cinemaId;
    }

    @PostMapping("/delete/{id}")
    public String hallDelete(@PathVariable long id) {
        logger.info("Entering hallDelete method with ID: {}", id);
        hallService.findById(id).ifPresent(hall -> {
            hallService.delete(hall);
            logger.info("Deleted hall with ID: {}", id);
        });
        logger.info("Exiting hallDelete method");
        return "redirect:/admin/cinemas";
    }
}
