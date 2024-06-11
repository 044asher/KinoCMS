package com.CMS.kinoCMS.controllers.Cinemas;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Hall;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.HallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cinemas/halls")
public class HallController {
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
        Optional<Hall> hall = hallService.findById(id);
        hall.ifPresent(value -> model.addAttribute("hall", value));
        return "cinemas/halls/hallEdit";
    }

    @PostMapping("/edit/{id}")
    public String hallSubmit(@PathVariable long id,
                             @Valid @ModelAttribute("hall") Hall hall,
                             @RequestParam(required = false) MultipartFile scheme,
                             @RequestParam(required = false) MultipartFile topBanner,
                             RedirectAttributes redirectAttributes) {

        try {
            Hall existingHall = hallService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Hall with id " + id + " not found"));

            existingHall.setNumber(hall.getNumber());
            existingHall.setDescription(hall.getDescription());

            if (scheme != null && !scheme.isEmpty()) {
                String resultSchemeName = fileUploadService.uploadFile(scheme);
                existingHall.setScheme(resultSchemeName);
            }

            if (topBanner != null && !topBanner.isEmpty()) {
                String resultTopBannerName = fileUploadService.uploadFile(topBanner);
                existingHall.setTopBanner(resultTopBannerName);
            }

            existingHall.setUrlSEO(hall.getUrlSEO());
            existingHall.setDescriptionSEO(hall.getDescriptionSEO());
            existingHall.setKeywordsSEO(hall.getKeywordsSEO());
            existingHall.setTitleSEO(hall.getTitleSEO());

            hallService.save(existingHall);

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error while saving hall: " + e.getMessage());
            return "redirect:/admin/cinemas/halls/edit/" + id;
        }

        return "redirect:/admin/cinemas";
    }

    @GetMapping("/add/{cinemaId}")
    public String hallAdd(@PathVariable long cinemaId, Model model){
        Optional<Cinema> cinema = cinemaService.findById(cinemaId);
        if (cinema.isPresent()) {
            model.addAttribute("cinema", cinema.get());
            model.addAttribute("hall", new Hall());
        }
        return "cinemas/halls/hallAdd";
    }

    @PostMapping("/add/{cinemaId}")
    public String hallAdd(@PathVariable long cinemaId, @Valid @ModelAttribute Hall hall,
                          @RequestParam("scheme") MultipartFile scheme,
                          @RequestParam("topBanner") MultipartFile topBanner,
                          RedirectAttributes redirectAttributes) {
        try {
            Optional<Cinema> cinema = cinemaService.findById(cinemaId);
            if (cinema.isPresent()) {
                if (scheme != null && !scheme.isEmpty()) {
                    String resultSchemeName = fileUploadService.uploadFile(scheme);
                    hall.setScheme(resultSchemeName);
                }
                if (topBanner != null && !topBanner.isEmpty()) {
                    String resultTopBannerName = fileUploadService.uploadFile(topBanner);
                    hall.setTopBanner(resultTopBannerName);
                }
                hall.setCinema(cinema.get());
                hall.setCreationDate(LocalDate.now());
                hallService.save(hall);
                redirectAttributes.addFlashAttribute("successMessage", "Зал успешно добавлен");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Кинотеатр не найден");
                return "redirect:/admin/cinemas";
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении зала: " + e.getMessage());
        }
        return "redirect:/admin/cinemas/edit/" + cinemaId;
    }

    @PostMapping("/delete/{id}")
    public String hallDelete(@PathVariable long id) {
        hallService.findById(id).ifPresent(hallService::delete);
        return "redirect:/admin/cinemas";
    }
}


