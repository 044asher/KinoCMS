package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Hall;
import com.CMS.kinoCMS.repositories.HallRepository;
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
        log.info("Start HallService - save. Hall ID: {}", hall.getId());
        hallRepository.save(hall);
        log.info("End HallService - save. Successfully saved hall with ID: {}", hall.getId());
    }

    public List<Hall> findAll() {
        log.info("Start HallService - findAll");
        List<Hall> halls = hallRepository.findAll();
        log.info("End HallService - findAll. Retrieved {} halls", halls.size());
        return halls;
    }

    public Optional<Hall> findById(long id) {
        log.info("Start HallService - findById for hall ID: {}", id);
        Optional<Hall> hall = hallRepository.findById(id);
        if (hall.isPresent()) {
            log.info("End HallService - findById. Found hall with ID: {}", id);
        } else {
            log.warn("End HallService - findById. No hall found with ID: {}", id);
        }
        return hall;
    }

    public List<Hall> findAllByCinemaId(long id) {
        log.info("Start HallService - findAllByCinemaId for cinema ID: {}", id);
        List<Hall> halls = hallRepository.findAllByCinemaId(id);
        log.info("End HallService - findAllByCinemaId. Retrieved {} halls for cinema ID: {}", halls.size(), id);
        return halls;
    }

    public void delete(Optional<Hall> hall) {
        log.info("Start HallService - delete. Hall present: {}", hall.isPresent());
        if (hall.isPresent()) {
            hallRepository.delete(hall.get());
            log.info("End HallService - delete. Successfully deleted hall with ID: {}", hall.get().getId());
        } else {
            log.warn("End HallService - delete. Attempted to delete non-existent hall");
        }
    }

    public void delete(Hall hall) {
        log.info("Start HallService - delete for hall ID: {}", hall.getId());
        hallRepository.delete(hall);
        log.info("End HallService - delete. Successfully deleted hall with ID: {}", hall.getId());
    }
}
