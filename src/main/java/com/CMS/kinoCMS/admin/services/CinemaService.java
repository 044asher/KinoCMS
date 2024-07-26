package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.CinemaRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final HallService hallService;
    private final FileUploadService fileUploadService;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository, HallService hallService, FileUploadService fileUploadService) {
        this.cinemaRepository = cinemaRepository;
        this.hallService = hallService;
        this.fileUploadService = fileUploadService;
    }

    public List<Cinema> findAll() {
        log.info("Fetching all cinemas");
        return cinemaRepository.findAll();
    }

    public void save(Cinema cinema) {
        log.info("Saving cinema: {}", cinema);
        cinemaRepository.save(cinema);
    }

    public Optional<Cinema> findById(Long id) {
        log.info("Finding cinema with id: {}", id);
        return cinemaRepository.findById(id);
    }

    public void delete(Cinema cinema) {
        log.info("Deleting cinema: {}", cinema);
        cinemaRepository.delete(cinema);
    }

    public void saveCinema(Cinema cinema, MultipartFile logo, MultipartFile banner, MultipartFile[] additionalFiles,
                           List<Integer> hallNumber, List<String> hallDescription, List<MultipartFile> hallScheme,
                           List<MultipartFile> hallBanner, List<String> urlSeo, List<String> titleSeo,
                           List<String> keywordsSeo, List<String> descriptionSeo) throws IOException {

        log.info("Saving new cinema: {}", cinema);

        if (logo != null && !logo.isEmpty()) {
            String resultLogoName = fileUploadService.uploadFile(logo);
            cinema.setLogoName(resultLogoName);
            log.info("Uploaded logo for cinema: {}", resultLogoName);
        }

        if (banner != null && !banner.isEmpty()) {
            String resultBannerName = fileUploadService.uploadFile(banner);
            cinema.setBannerName(resultBannerName);
            log.info("Uploaded banner for cinema: {}", resultBannerName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            cinema.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Uploaded additional files for cinema: {}", newImageNames);
        }

        save(cinema);

        if (hallNumber != null && !hallNumber.isEmpty()) {
            for (int i = 0; i < hallNumber.size(); i++) {
                Hall hall = new Hall();
                hall.setNumber(hallNumber.get(i));
                hall.setDescription(hallDescription.get(i));
                hall.setCinema(cinema);

                if (hallScheme != null && i < hallScheme.size() && !hallScheme.get(i).isEmpty()) {
                    String resultSchemeName = fileUploadService.uploadFile(hallScheme.get(i));
                    hall.setScheme(resultSchemeName);
                    log.info("Uploaded scheme for hall {}: {}", hallNumber.get(i), resultSchemeName);
                }

                if (hallBanner != null && i < hallBanner.size() && !hallBanner.get(i).isEmpty()) {
                    String resultBannerName = fileUploadService.uploadFile(hallBanner.get(i));
                    hall.setTopBanner(resultBannerName);
                    log.info("Uploaded banner for hall {}: {}", hallNumber.get(i), resultBannerName);
                }

                hall.setUrlSEO(urlSeo.get(i));
                hall.setTitleSEO(titleSeo.get(i));
                hall.setKeywordsSEO(keywordsSeo.get(i));
                hall.setDescriptionSEO(descriptionSeo.get(i));
                hall.setCreationDate(LocalDate.now());

                hallService.save(hall);
                log.info("Saved hall with number: {}", hallNumber.get(i));
            }
        }
    }

    public void updateCinema(Long id, Cinema cinema, MultipartFile logo, MultipartFile banner, MultipartFile[] additionalFiles) throws IOException {
        log.info("Updating cinema with id: {}", id);
        Cinema existingCinema = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema Id: " + id));

        existingCinema.setName(cinema.getName());
        existingCinema.setDescription(cinema.getDescription());
        existingCinema.setConditions(cinema.getConditions());

        if (logo != null && !logo.isEmpty()) {
            String resultLogoName = fileUploadService.uploadFile(logo);
            existingCinema.setLogoName(resultLogoName);
            log.info("Updated logo for cinema: {}", resultLogoName);
        }

        if (banner != null && !banner.isEmpty()) {
            String resultBannerName = fileUploadService.uploadFile(banner);
            existingCinema.setBannerName(resultBannerName);
            log.info("Updated banner for cinema: {}", resultBannerName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingCinema.getImages().clear();
            existingCinema.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Updated additional files for cinema: {}", newImageNames);
        }

        existingCinema.setUrlSEO(cinema.getUrlSEO());
        existingCinema.setTitleSEO(cinema.getTitleSEO());
        existingCinema.setKeywordsSEO(cinema.getKeywordsSEO());
        existingCinema.setDescriptionSEO(cinema.getDescriptionSEO());

        save(existingCinema);
    }
}
