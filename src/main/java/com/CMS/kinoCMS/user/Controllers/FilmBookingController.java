package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Reservation;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.models.Seat;
import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.SeatRepository;
import com.CMS.kinoCMS.admin.services.ReservationService;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import com.CMS.kinoCMS.config.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/film-booking")
public class FilmBookingController {

    private final ReservationService reservationService;
    private final ScheduleService scheduleService;
    private final SeatRepository seatRepository;

    @Autowired
    public FilmBookingController(ReservationService reservationService, ScheduleService scheduleService, SeatRepository seatRepository) {
        this.reservationService = reservationService;
        this.scheduleService = scheduleService;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/{scheduleId}")
    public String getBookingPage(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.findById(scheduleId).orElseThrow();
        List<Reservation> reservations = reservationService.getReservationsBySchedule(schedule);

        model.addAttribute("schedule", schedule);
        model.addAttribute("reservations", reservations);
        model.addAttribute("film", schedule.getFilm());
        model.addAttribute("hall", schedule.getHall());


        model.addAttribute("seats", seatRepository.findAll());

        return "users-part/booking/film-booking";
    }

    @PostMapping("/{scheduleId}/reserve")
    public ResponseEntity<?> reserveSeat(@PathVariable Long scheduleId, @RequestParam Long seatId, Authentication authentication) {
        Optional<Schedule> schedule = scheduleService.findById(scheduleId);
        Seat seat = seatRepository.findById(seatId).orElseThrow();
        User user = ((MyUserDetails) authentication.getPrincipal()).getUser();

        Reservation reservation = new Reservation();
        reservation.setSchedule(schedule.get());
        reservation.setSeat(seat);
        reservation.setUser(user);

        reservationService.saveReservation(reservation);

        return ResponseEntity.ok("Seat reserved successfully");
    }
}
