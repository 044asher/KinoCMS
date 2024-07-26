package com.CMS.kinoCMS.admin.controllers.Cinemas;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.HallService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/cinemas")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CinemaController {
    private final CinemaService cinemaService;
    private final HallService hallService;
    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    @Autowired
    public CinemaController(CinemaService cinemaService, HallService hallService) {
        this.cinemaService = cinemaService;
        this.hallService = hallService;
    }

    @GetMapping
    public String allCinemas(Model model) {
        try {
            model.addAttribute("cinemas", cinemaService.findAll());
        } catch (Exception e) {
            logger.error("Failed to retrieve cinemas: {}", e.getMessage());
        }
        return "/cinemas/cinemas";
    }


    @GetMapping("/add")
    public String addCinema(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "/cinemas/add";
    }

    @PostMapping("/add")
    public String addCinema(@Valid @ModelAttribute("cinema") Cinema cinema,
                            BindingResult bindingResult,
                            @RequestParam("logo") MultipartFile logo,
                            @RequestParam("banner") MultipartFile banner,
                            @RequestParam("additionalFiles") MultipartFile[] additionalFiles,
                            @RequestParam(required = false) List<Integer> hallNumber,
                            @RequestParam(required = false) List<String> hallDescription,
                            @RequestParam(required = false) List<MultipartFile> hallScheme,
                            @RequestParam(required = false) List<MultipartFile> hallBanner,
                            @RequestParam(required = false) List<String> urlSeo,
                            @RequestParam(required = false) List<String> titleSeo,
                            @RequestParam(required = false) List<String> keywordsSeo,
                            @RequestParam(required = false) List<String> descriptionSeo) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "/cinemas/add";
        }
        try {
            cinemaService.saveCinema(cinema, logo, banner, additionalFiles, hallNumber, hallDescription, hallScheme, hallBanner, urlSeo, titleSeo, keywordsSeo, descriptionSeo);
        } catch (IOException e) {
            logger.error("Error uploading files or saving cinema/hall: {}", e.getMessage());
        }
        return "redirect:/admin/cinemas";
    }

    @PostMapping("/{id}/delete")
    public String deleteCinema(@PathVariable Long id) {
        try {
            Optional<Cinema> cinema = cinemaService.findById(id);
            cinema.ifPresent(cinemaService::delete);
        } catch (Exception e) {
            logger.error("Failed to delete cinema with ID: {}", id);
        }
        return "redirect:/admin/cinemas";
    }


    @GetMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id, Model model) {
        try {
            Optional<Cinema> optionalCinema = cinemaService.findById(id);
            if (optionalCinema.isPresent()) {
                model.addAttribute("cinema", optionalCinema.get());
                model.addAttribute("halls", hallService.findAllByCinemaId(optionalCinema.get().getId()));
            } else {
                logger.warn("Cinema not found, ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Failed to load cinema for editing, ID: {}", id);
        }
        return "cinemas/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id,
                             @Valid @ModelAttribute("cinema") Cinema cinema,
                             BindingResult bindingResult,
                             @RequestParam(required = false) MultipartFile logo,
                             @RequestParam(required = false) MultipartFile banner,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "cinemas/edit";
        }
        try {
            cinemaService.updateCinema(id, cinema, logo, banner, additionalFiles);
        } catch (IOException e) {
            logger.error("Error uploading files or saving cinema: {}", e.getMessage());
            return "cinemas/edit";
        }
        return "redirect:/admin/cinemas";
    }
}
