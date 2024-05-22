package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    public List<Film> getCurrentFilms(){
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateBefore(today);
    }

    public List<Film> getUpcomingFilms(){
        LocalDate today = LocalDate.now();
        return filmRepository.findByDateAfterOrDateIsNull(today);
    }
}
