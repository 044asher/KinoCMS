package com.CMS.kinoCMS.controllers.Public;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Schedule;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FilmService;
import com.CMS.kinoCMS.services.HallService;
import com.CMS.kinoCMS.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    private final CinemaService cinemaService;
    private final ScheduleService scheduleService;
    private final FilmService filmService;
    private final HallService hallService;

    @Autowired
    public ScheduleController(CinemaService cinemaService, ScheduleService scheduleService, FilmService filmService, HallService hallService) {
        this.cinemaService = cinemaService;
        this.scheduleService = scheduleService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping
    public String schedule(Model model) {
        model.addAttribute("cinemas", cinemaService.findAll());
        return "users-part/schedules/cinemas-list";
    }

    @GetMapping("/{id}")
    public String schedule(@PathVariable Long id,
                           @RequestParam(required = false) Long filmId,
                           @RequestParam(required = false) Long hallId,
                           @RequestParam(required = false) String filmType,
                           Model model) {
        Optional<Cinema> cinema = cinemaService.findById(id);


        List<Schedule> filteredSchedules = scheduleService.getFilteredSchedules(id, filmId, hallId, filmType);
        Map<LocalDate, List<Schedule>> schedulesByDate = scheduleService.groupSchedulesByDate(filteredSchedules);
        // Сортировка по датам
        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        model.addAttribute("cinema", cinema.orElseThrow());
        model.addAttribute("schedulesByDate", sortedSchedulesByDate);
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("halls", hallService.findAll());
        model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMax"));

        model.addAttribute("filmId", filmId);
        model.addAttribute("hallId", hallId);
        model.addAttribute("filmType", filmType);
        return "users-part/schedules/schedule-list";
    }
}
