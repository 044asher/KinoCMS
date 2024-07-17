package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.models.Seat;
import com.CMS.kinoCMS.admin.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Optional<Seat> findById(Long seatId) {
        return seatRepository.findById(seatId);
    }

    public void save(Seat seat) {
        seatRepository.save(seat);
    }

    public List<Seat> findAll() {
        return seatRepository.findAll();
    }
}
