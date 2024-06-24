package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HallService {
    private final HallRepository hallRepository;

    @Autowired
    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public void save(Hall hall) {
        hallRepository.save(hall);
    }

    public List<Hall> findAll() {
        return hallRepository.findAll();
    }

    public Optional<Hall> findById(long id) {
        return hallRepository.findById(id);
    }
    public List<Hall> findAllByCinemaId(long id) {
        return hallRepository.findAllByCinemaId(id);
    }

    public void delete(Optional<Hall> hall) {
        hallRepository.delete(hall.get());
    }

    public void delete(Hall hall) {
        hallRepository.delete(hall);
    }
}
