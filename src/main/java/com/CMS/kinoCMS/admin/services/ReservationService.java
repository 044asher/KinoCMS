package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Reservation;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getReservationsBySchedule(Schedule schedule) {
        return reservationRepository.findBySchedule(schedule);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
