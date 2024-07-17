package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.repositories.FilmRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FileUploadService fileUploadService;

    @Autowired
    public FilmService(FilmRepository filmRepository, FileUploadService fileUploadService) {
        this.filmRepository = filmRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<Film> getCurrentFilms() {
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateBeforeOrDate(today, today);
    }

    public List<Film> getUpcomingFilms() {
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateAfterOrDateIsNull(today);
    }

    public List<Film> getPrePremieresFilms(boolean prePremiere) {
        return filmRepository.findByIsPrePremiere(prePremiere);
    }

    public void save(Film film) {
        filmRepository.save(film);
    }

    public Optional<Film> findById(Long id) {
        return filmRepository.findById(id);
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public void processFilmData(Film film, MultipartFile mainFile, MultipartFile[] additionalFiles,
                                List<String> filmTypes, Integer year, String country, String musician,
                                List<String> producer, String director, List<String> writer,
                                List<String> genre, Integer age, Integer time) throws IOException {
        log.info("Processing film data for: {}", film.getName());

        if (mainFile != null && !mainFile.isEmpty()) {
            String resultFileName = fileUploadService.uploadFile(mainFile);
            film.setMainImage(resultFileName);
            log.info("Main image uploaded for film: {}", film.getName());
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            film.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Additional images uploaded for film: {}", film.getName());
        }

        film.setTypes(String.join(",", filmTypes));

        if (year != null) film.setYear(year);
        if (country != null) film.setCountry(country);
        if (musician != null) film.setMusician(musician);
        if (producer != null) film.setProducer(producer);
        if (director != null) film.setDirector(director);
        if (writer != null) film.setWriter(writer);
        if (genre != null) film.setGenre(genre);
        if (age != null) film.setAge(age);
        if (time != null) film.setTime(time);

        save(film);
        log.info("Film data processed and saved: {}", film.getName());
    }

    public void updateFilm(Long id, Film film, MultipartFile mainFile, MultipartFile[] additionalFiles,
                           List<String> filmTypes, Integer year, String country, String musician,
                           List<String> producer, String director, List<String> writer,
                           List<String> genre, Integer age, Integer time) throws IOException {
        Film existingFilm = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

        log.info("Updating film with id: {}", id);

        if (mainFile != null && !mainFile.isEmpty()) {
            String resultFileName = fileUploadService.uploadFile(mainFile);
            existingFilm.setMainImage(resultFileName);
            log.info("Main image updated for film: {}", existingFilm.getName());
        }

        if (additionalFiles != null && additionalFiles.length > 0) {
            List<String> newImageNames = fileUploadService.uploadAdditionalFiles(additionalFiles);
            existingFilm.getImages().clear();
            existingFilm.getImages().addAll(newImageNames.stream().limit(5).toList());
            log.info("Additional images updated for film: {}", existingFilm.getName());
        }

        existingFilm.setName(film.getName());
        existingFilm.setDescription(film.getDescription());
        existingFilm.setLink(film.getLink());
        if (film.getDate() != null) {
            existingFilm.setDate(film.getDate());
        }
        existingFilm.setUrlSEO(film.getUrlSEO());
        existingFilm.setTitleSEO(film.getTitleSEO());
        existingFilm.setKeywordsSEO(film.getKeywordsSEO());
        existingFilm.setDescriptionSEO(film.getDescriptionSEO());

        if (filmTypes != null) {
            String typesString = String.join(",", filmTypes);
            existingFilm.setTypes(typesString);
        } else {
            existingFilm.setTypes("");
        }

        existingFilm.setYear(year != null ? year : existingFilm.getYear());
        existingFilm.setCountry(country);
        existingFilm.setMusician(musician);
        existingFilm.setProducer(producer != null ? new ArrayList<>(Collections.singletonList(String.join(",", producer))) : null);
        existingFilm.setDirector(director);
        existingFilm.setWriter(writer != null ? new ArrayList<>(Collections.singletonList(String.join(",", writer))) : null);
        existingFilm.setGenre(genre != null ? new ArrayList<>(Collections.singletonList(String.join(",", genre))) : null);
        existingFilm.setAge(age != null ? age : existingFilm.getAge());
        existingFilm.setTime(time != null ? time : existingFilm.getTime());

        if (film.getDate() != null && film.getDate().isBefore(LocalDate.now())) {
            existingFilm.setPrePremiere(false);
        } else {
            existingFilm.setPrePremiere(film.isPrePremiere());
        }

        save(existingFilm);
        log.info("Film updated and saved: {}", existingFilm.getName());
    }
}
