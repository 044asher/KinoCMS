package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.repositories.FilmRepository;
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
        log.info("Start FilmService - getCurrentFilms");
        LocalDate today = LocalDate.now();
        List<Film> films = filmRepository.findByDateBeforeOrDate(today, today);
        log.info("Successfully executed FilmService - getCurrentFilms");
        return films;
    }

    public List<Film> getUpcomingFilms() {
        log.info("Start FilmService - getUpcomingFilms");
        LocalDate today = LocalDate.now();
        List<Film> films = filmRepository.findByDateAfterOrDateIsNull(today);
        log.info("Successfully executed FilmService - getUpcomingFilms");
        return films;
    }

    public List<Film> getPrePremieresFilms(boolean prePremiere) {
        log.info("Start FilmService - getPrePremieresFilms");
        List<Film> films = filmRepository.findByIsPrePremiere(prePremiere);
        log.info("Successfully executed FilmService - getPrePremieresFilms");
        return films;
    }

    public void save(Film film) {
        log.info("Start FilmService - save");
        filmRepository.save(film);
        log.info("Successfully executed FilmService - save");
    }

    public Optional<Film> findById(Long id) {
        log.info("Start FilmService - findById with id: {}", id);
        Optional<Film> film = filmRepository.findById(id);
        log.info("Successfully executed FilmService - findById with id: {}", id);
        return film;
    }

    public List<Film> findAll() {
        log.info("Start FilmService - findAll");
        List<Film> all = filmRepository.findAll();
        log.info("Successfully executed FilmService - findAll");
        return all;
    }

    public void processFilmData(Film film, MultipartFile mainFile, MultipartFile[] additionalFiles,
                                List<String> filmTypes, Integer year, String country, String musician,
                                List<String> producer, String director, List<String> writer,
                                List<String> genre, Integer age, Integer time) throws IOException {
        log.info("Start FilmService - processFilmData for film: {}", film.getName());

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
        log.info("Successfully executed FilmService - processFilmData for film: {}", film.getName());
    }

    public void updateFilm(Long id, Film film, MultipartFile mainFile, MultipartFile[] additionalFiles,
                           List<String> filmTypes, Integer year, String country, String musician,
                           List<String> producer, String director, List<String> writer,
                           List<String> genre, Integer age, Integer time) throws IOException {
        log.info("Start FilmService - updateFilm with id: {}", id);

        Film existingFilm = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

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
        log.info("Successfully executed FilmService - updateFilm with id: {}", id);
    }
}
