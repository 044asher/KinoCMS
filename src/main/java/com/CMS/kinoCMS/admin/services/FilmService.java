package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getCurrentFilms(){
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateBeforeOrDate(today, today);
    }

    public List<Film> getUpcomingFilms(){
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateAfterOrDateIsNull(today);
    }
    public void save(Film film){
        filmRepository.save(film);
    }
    public Optional<Film> findById(Long id){
        return filmRepository.findById(id);
    }
}
