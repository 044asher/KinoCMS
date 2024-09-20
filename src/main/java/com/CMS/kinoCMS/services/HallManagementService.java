package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Hall;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class HallManagementService {
    private final HallService hallService;
    private final CinemaService cinemaService;
    private final FileUploadService fileUploadService;

    @Autowired
    public HallManagementService(HallService hallService, CinemaService cinemaService, FileUploadService fileUploadService) {
        this.hallService = hallService;
        this.cinemaService = cinemaService;
        this.fileUploadService = fileUploadService;
    }

    public void updateHall(Long id, Hall hall, MultipartFile logo, MultipartFile banner, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start HallManagementService - updateHall for hall id: {}", id);

        Hall existingHall = hallService.findById(id)
                .orElseThrow(() -> {
                    log.error("Hall with id {} not found", id);
                    return new IllegalArgumentException("Hall with id " + id + " not found");
                });

        existingHall.setNumber(hall.getNumber());
        existingHall.setDescription(hall.getDescription());

        if (logo != null && !logo.isEmpty()) {
            String resultLogoName = fileUploadService.uploadFile(logo);
            existingHall.setScheme(resultLogoName);
            log.info("Updated hall {}: Set new logo {}", id, resultLogoName);
        }

        if (banner != null && !banner.isEmpty()) {
            String resultBannerName = fileUploadService.uploadFile(banner);
            existingHall.setTopBanner(resultBannerName);
            log.info("Updated hall {}: Set new banner {}", id, resultBannerName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingHall.getImages().clear();
            existingHall.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Updated hall {}: Added {} new images", id, newImageNames.size());
        }

        existingHall.setUrlSEO(hall.getUrlSEO());
        existingHall.setDescriptionSEO(hall.getDescriptionSEO());
        existingHall.setKeywordsSEO(hall.getKeywordsSEO());
        existingHall.setTitleSEO(hall.getTitleSEO());

        hallService.save(existingHall);
        log.info("End HallManagementService - updateHall for hall id: {}. Hall updated successfully", id);
    }

    public void addHall(Long cinemaId, Hall hall, MultipartFile logo, MultipartFile banner, MultipartFile[] additionalFiles) throws IOException {
        log.info("Start HallManagementService - addHall for cinema id: {}", cinemaId);

        Cinema cinema = cinemaService.findById(cinemaId)
                .orElseThrow(() -> {
                    log.error("Cinema with id {} not found", cinemaId);
                    return new IllegalArgumentException("Invalid cinema Id: " + cinemaId);
                });

        if (logo != null && !logo.isEmpty()) {
            String resultLogoName = fileUploadService.uploadFile(logo);
            hall.setScheme(resultLogoName);
            log.info("Adding new hall: Set logo {}", resultLogoName);
        }

        if (banner != null && !banner.isEmpty()) {
            String resultBannerName = fileUploadService.uploadFile(banner);
            hall.setTopBanner(resultBannerName);
            log.info("Adding new hall: Set banner {}", resultBannerName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            hall.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Adding new hall: Added {} images", newImageNames.size());
        }

        hall.setCinema(cinema);
        hall.setCreationDate(LocalDate.now());
        hallService.save(hall);
        log.info("End HallManagementService - addHall for cinema id: {}. New hall added successfully", cinemaId);
    }
}
