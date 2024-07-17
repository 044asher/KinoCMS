package com.CMS.kinoCMS.admin.controllers.Cinemas;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
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
    private final FileUploadService fileUploadService;
    private final HallService hallService;
    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);


    @Autowired
    public CinemaController(CinemaService cinemaService, FileUploadService fileUploadService, HallService hallService) {
        this.cinemaService = cinemaService;
        this.fileUploadService = fileUploadService;
        this.hallService = hallService;
    }

    @GetMapping
    public String allCinemas(Model model) {
        logger.info("Entering allCinemas method");

        try {
            model.addAttribute("cinemas", cinemaService.findAll());
            logger.info("Retrieved all cinemas");
        } catch (Exception e) {
            logger.error("Failed to retrieve cinemas: {}", e.getMessage());
        }

        logger.info("Exiting allCinemas method");
        return "/cinemas/cinemas";
    }


    @GetMapping("/add")
    public String addCinema(Model model) {
        logger.info("Entering addCinema (GET) method");
        model.addAttribute("cinema", new Cinema());
        logger.info("Exiting addCinema (GET) method");
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
        logger.info("Entering addCinema (POST) method");

        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "/cinemas/add";
        }

        try {
            cinemaService.saveCinema(cinema, logo, banner, additionalFiles, hallNumber, hallDescription, hallScheme, hallBanner, urlSeo, titleSeo, keywordsSeo, descriptionSeo);
            logger.info("Saved cinema: {}", cinema.getName());
        } catch (IOException e) {
            logger.error("Error uploading files or saving cinema/hall: {}", e.getMessage());
        }

        logger.info("Exiting addCinema (POST) method");
        return "redirect:/admin/cinemas";
    }

    @PostMapping("/{id}/delete")
    public String deleteCinema(@PathVariable Long id) {
        logger.info("Entering deleteCinema method for ID: {}", id);

        try {
            Optional<Cinema> cinema = cinemaService.findById(id);
            cinema.ifPresent(cinemaService::delete);
            logger.info("Deleted cinema with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete cinema with ID: {}", id);
        }

        logger.info("Exiting deleteCinema method");
        return "redirect:/admin/cinemas";
    }


    @GetMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id, Model model) {
        logger.info("Entering editCinema (GET) method for ID: {}", id);

        try {
            Optional<Cinema> optionalCinema = cinemaService.findById(id);
            if (optionalCinema.isPresent()) {
                model.addAttribute("cinema", optionalCinema.get());
                model.addAttribute("halls", hallService.findAllByCinemaId(optionalCinema.get().getId()));
                logger.info("Loaded cinema and halls for editing, ID: {}", id);
            } else {
                logger.warn("Cinema not found, ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Failed to load cinema for editing, ID: {}", id);
        }

        logger.info("Exiting editCinema (GET) method");
        return "cinemas/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id,
                             @Valid @ModelAttribute("cinema") Cinema cinema,
                             BindingResult bindingResult,
                             @RequestParam(required = false) MultipartFile logo,
                             @RequestParam(required = false) MultipartFile banner,
                             @RequestParam(required = false, value = "additionalFiles") MultipartFile[] additionalFiles) {
        logger.info("Entering editCinema (POST) method for ID: {}", id);

        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has errors: {}", bindingResult.getAllErrors());
            return "cinemas/edit";
        }

        try {
            Cinema existingCinema = cinemaService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid cinema Id:" + id));

            existingCinema.setName(cinema.getName());
            existingCinema.setDescription(cinema.getDescription());
            existingCinema.setConditions(cinema.getConditions());

            if (logo != null && !logo.isEmpty()) {
                String resultLogoName = fileUploadService.uploadFile(logo);
                existingCinema.setLogoName(resultLogoName);
                logger.info("Uploaded new logo for cinema, ID: {}", id);
            }

            if (banner != null && !banner.isEmpty()) {
                String resultBannerName = fileUploadService.uploadFile(banner);
                existingCinema.setBannerName(resultBannerName);
                logger.info("Uploaded new banner for cinema, ID: {}", id);
            }

            if (additionalFiles != null && additionalFiles.length > 0) {
                List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
                existingCinema.getImages().clear();
                existingCinema.getImages().addAll(newImageNames.stream().limit(5).toList());
                logger.info("Uploaded additional files for cinema with id: {}", id);
            }


            existingCinema.setUrlSEO(cinema.getUrlSEO());
            existingCinema.setTitleSEO(cinema.getTitleSEO());
            existingCinema.setKeywordsSEO(cinema.getKeywordsSEO());
            existingCinema.setDescriptionSEO(cinema.getDescriptionSEO());

            cinemaService.save(existingCinema);
            logger.info("Updated cinema with ID: {}", id);
        } catch (IOException e) {
            logger.error("Error uploading files or saving cinema: {}", e.getMessage());
            return "cinemas/edit";
        }

        logger.info("Exiting editCinema (POST) method");
        return "redirect:/admin/cinemas";
    }
}
