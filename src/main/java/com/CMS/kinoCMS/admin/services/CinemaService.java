package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.CinemaRepository;
import com.CMS.kinoCMS.admin.repositories.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final HallService hallService;
    private final FileUploadService fileUploadService;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository, HallRepository hallRepository, HallService hallService, FileUploadService fileUploadService) {
        this.cinemaRepository = cinemaRepository;
        this.hallService = hallService;
        this.fileUploadService = fileUploadService;
    }

    public List<Cinema> findAll() {
        return cinemaRepository.findAll();
    }

    public void save(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    public Optional<Cinema> findById(Long id) {
       return cinemaRepository.findById(id);
    }

    public void delete(Cinema cinema) {
        cinemaRepository.delete(cinema);
    }

    public void saveCinema(Cinema cinema, MultipartFile logo, MultipartFile banner, MultipartFile[] additionalFiles,
                           List<Integer> hallNumber, List<String> hallDescription, List<MultipartFile> hallScheme,
                           List<MultipartFile> hallBanner, List<String> urlSeo, List<String> titleSeo,
                           List<String> keywordsSeo, List<String> descriptionSeo) throws IOException {

        if (!logo.isEmpty()) {
            String resultLogoName = fileUploadService.uploadFile(logo);
            cinema.setLogoName(resultLogoName);
        }

        if (!banner.isEmpty()) {
            String resultBannerName = fileUploadService.uploadFile(banner);
            cinema.setBannerName(resultBannerName);
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            cinema.getImages().addAll(newImageNames.stream().limit(5).toList());
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
    }

    public void updateCinema(long id, CinemaUpdateDto cinemaUpdateDto, MultipartFile logoFile, FileUploadService fileUploadService) throws IOException {
        Cinema existingCinema = findById(id).orElseThrow(() -> new RuntimeException("Cinema Not Found"));
        if (logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileUploadService.uploadFile(logoFile);
            existingCinema.setLogoName(fileName);
        }
        existingCinema.setAddress(cinemaUpdateDto.getAddress());
        existingCinema.setXCoordinate(cinemaUpdateDto.getXCoordinate());
        existingCinema.setYCoordinate(cinemaUpdateDto.getYCoordinate());
        save(existingCinema);
    }
}
