package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.repositories.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
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

    public void delete(Optional<Cinema> cinema) {
        cinemaRepository.delete(cinema.get());
    }
}
