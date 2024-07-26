package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.HallRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class HallService {
    private final HallRepository hallRepository;

    @Autowired
    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public void save(Hall hall) {
        hallRepository.save(hall);
        log.info("Saved hall with id {}", hall.getId());
    }

    public List<Hall> findAll() {
        List<Hall> halls = hallRepository.findAll();
        log.info("Retrieved {} halls", halls.size());
        return halls;
    }

    public Optional<Hall> findById(long id) {
        Optional<Hall> hall = hallRepository.findById(id);
        if (hall.isPresent()) {
            log.info("Found hall with id {}", id);
        } else {
            log.warn("No hall found with id {}", id);
        }
        return hall;
    }

    public List<Hall> findAllByCinemaId(long id) {
        List<Hall> halls = hallRepository.findAllByCinemaId(id);
        log.info("Retrieved {} halls for cinema with id {}", halls.size(), id);
        return halls;
    }

    public void delete(Optional<Hall> hall) {
        if (hall.isPresent()) {
            hallRepository.delete(hall.get());
            log.info("Deleted hall with id {}", hall.get().getId());
        } else {
            log.warn("Attempted to delete non-existent hall");
        }
    }

    public void delete(Hall hall) {
        hallRepository.delete(hall);
        log.info("Deleted hall with id {}", hall.getId());
    }
}
