package com.CMS.kinoCMS.controllers.Cinemas;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Hall;
import com.CMS.kinoCMS.repositories.CityRepository;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.HallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/cinemas")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CinemaController {
    private final CinemaService cinemaService;
    private final FileUploadService fileUploadService;
    private final HallService hallService;

    @Autowired
    public CinemaController(CinemaService cinemaService, CityRepository cityRepository, FileUploadService fileUploadService, HallService hallService) {
        this.cinemaService = cinemaService;
        this.fileUploadService = fileUploadService;
        this.hallService = hallService;
    }

    @GetMapping
    public String allCinemas(Model model) {
        model.addAttribute("cinemas", cinemaService.findAll());
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

                            @RequestParam(required = false) List<Integer> hallNumber,
                            @RequestParam(required = false) List<String> hallDescription,
                            @RequestParam(required = false) List<MultipartFile> hallScheme,
                            @RequestParam(required = false) List<MultipartFile> hallBanner,
                            @RequestParam(required = false) List<String> urlSeo,
                            @RequestParam(required = false) List<String> titleSeo,
                            @RequestParam(required = false) List<String> keywordsSeo,
                            @RequestParam(required = false) List<String> descriptionSeo) {
        if (bindingResult.hasErrors()) {
            return "/cinemas/add";
        }

        try {
            if (!logo.isEmpty()) {
                String resultLogoName = fileUploadService.uploadFile(logo);
                cinema.setLogoName(resultLogoName);
            }
            if (!banner.isEmpty()) {
                String resultBannerName = fileUploadService.uploadFile(banner);
                cinema.setBannerName(resultBannerName);
            }
            cinemaService.save(cinema);

            if (hallNumber != null && !hallNumber.isEmpty()) {
                for (int i = 0; i < hallNumber.size(); i++) {
                    Hall hall = new Hall();
                    hall.setNumber(hallNumber.get(i));
                    hall.setDescription(hallDescription.get(i));
                    hall.setCinema(cinema);

                    if (hallScheme != null && i < hallScheme.size() && !hallScheme.get(i).isEmpty()) {
                        String resultSchemeName = fileUploadService.uploadFile(hallScheme.get(i));
                        hall.setScheme(resultSchemeName);
                    }
                    if (hallBanner != null && i < hallBanner.size() && !hallBanner.get(i).isEmpty()) {
                        String resultBannerName = fileUploadService.uploadFile(hallBanner.get(i));
                        hall.setTopBanner(resultBannerName);
                    }
                    hall.setUrlSEO(urlSeo.get(i));
                    hall.setTitleSEO(titleSeo.get(i));
                    hall.setKeywordsSEO(keywordsSeo.get(i));
                    hall.setDescriptionSEO(descriptionSeo.get(i));
                    hall.setCreationDate(LocalDate.now());
                    hallService.save(hall);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/cinemas";
    }

    @PostMapping("/{id}/delete")
    public String deleteCinema(@PathVariable Long id) {
        Optional<Cinema> cinema = cinemaService.findById(id);
        cinemaService.delete(cinema);
        return "redirect:/admin/cinemas";
    }


    @GetMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id, Model model) {
        Optional<Cinema> optionalCinema = cinemaService.findById(id);
        if (optionalCinema.isPresent()) {
            model.addAttribute("cinema", optionalCinema.get());
            model.addAttribute("halls", hallService.findAllByCinemaId(optionalCinema.get().getId()));
        }
        return "cinemas/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCinema(@PathVariable Long id,
                             @Valid @ModelAttribute("cinema") Cinema cinema,
                             BindingResult bindingResult,
                             @RequestParam(required = false) MultipartFile logo,
                             @RequestParam(required = false) MultipartFile banner) {
        if (bindingResult.hasErrors()) {
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
            }

            if (banner != null && !banner.isEmpty()) {
                String resultBannerName = fileUploadService.uploadFile(banner);
                existingCinema.setBannerName(resultBannerName);
            }

            existingCinema.setUrlSEO(cinema.getUrlSEO());
            existingCinema.setTitleSEO(cinema.getTitleSEO());
            existingCinema.setKeywordsSEO(cinema.getKeywordsSEO());
            existingCinema.setDescriptionSEO(cinema.getDescriptionSEO());

            cinemaService.save(existingCinema);
        } catch (IOException e) {
            e.printStackTrace();
            return "cinemas/edit";
        }

        return "redirect:/admin/cinemas";
    }
}
