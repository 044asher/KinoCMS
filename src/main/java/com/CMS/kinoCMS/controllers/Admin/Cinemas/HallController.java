package com.CMS.kinoCMS.controllers.Admin.Cinemas;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Hall;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.HallManagementService;
import com.CMS.kinoCMS.services.HallService;
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
import java.util.Optional;

@Controller
@RequestMapping("/admin/cinemas/halls")
public class HallController {
    private static final Logger logger = LoggerFactory.getLogger(HallController.class);

    private final HallService hallService;
    private final HallManagementService hallManagementService;
    private final CinemaService cinemaService;

    @Autowired
    public HallController(HallService hallService, HallManagementService hallManagementService, CinemaService cinemaService) {
        this.hallService = hallService;
        this.hallManagementService = hallManagementService;
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
                             @RequestParam(required = false) MultipartFile logo,
                             @RequestParam(required = false) MultipartFile banner,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles,
                             RedirectAttributes redirectAttributes) {
        try {
            hallManagementService.updateHall(id, hall, logo, banner, additionalFiles);
        } catch (IOException e) {
            logger.error("Error while saving hall with ID: {}: {}", id, e.getMessage());
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
        } else {
            logger.warn("Cinema not found for cinemaId: {}", cinemaId);
        }
        return "cinemas/halls/hallAdd";
    }

    @PostMapping("/add/{cinemaId}")
    public String hallAdd(@PathVariable long cinemaId, @Valid @ModelAttribute Hall hall,
                          @RequestParam("logo") MultipartFile logo,
                          @RequestParam("banner") MultipartFile banner,
                          @RequestParam("additionalFiles") MultipartFile[] additionalFiles,
                          RedirectAttributes redirectAttributes) {
        try {
            hallManagementService.addHall(cinemaId, hall, logo, banner, additionalFiles);
            redirectAttributes.addFlashAttribute("successMessage", "Зал успешно добавлен");
        } catch (IOException e) {
            logger.error("Ошибка при добавлении зала для cinemaId: {}: {}", cinemaId, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении зала: " + e.getMessage());
            return "redirect:/admin/cinemas/edit/" + cinemaId;
        }
        return "redirect:/admin/cinemas/edit/" + cinemaId;
    }

    @PostMapping("/delete/{id}")
    public String hallDelete(@PathVariable long id) {
        hallService.findById(id).ifPresent(hallService::delete);
        return "redirect:/admin/cinemas";
    }
}
