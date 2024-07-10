package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FilmService;
import com.CMS.kinoCMS.admin.services.HallService;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cinemas")
public class CinemaUserController {
    private final CinemaService cinemaService;
    private final ScheduleService scheduleService;
    private final HallService hallService;
    private final FilmService filmService;

    @Autowired
    public CinemaUserController(CinemaService cinemaService, ScheduleService scheduleService, HallService hallService, FilmService filmService) {
        this.cinemaService = cinemaService;
        this.scheduleService = scheduleService;
        this.hallService = hallService;
        this.filmService = filmService;
    }

    @GetMapping()
    public String cinemas(Model model) {
        List<Cinema> cinemas = cinemaService.findAll();
        model.addAttribute("cinemas", cinemas);
        return "users-part/cinemas/cinemas";
    }

    @GetMapping("/{id}")
    public String cinema(Model model, @PathVariable long id) {
        Optional<Cinema> cinema = cinemaService.findById(id);
        cinema.ifPresent(value -> model.addAttribute("cinema", value));

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Найти все расписания на сегодня
        List<Schedule> schedules = scheduleService.findByCinemaIdAndDateRange(id, today, today);

        // Отфильтровать сеансы, которые начинаются сегодня и не прошли по времени
        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> schedule.getDate().isEqual(today) && schedule.getTime().isAfter(currentTime))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();

        // Группировать сеансы по дате
        Map<LocalDate, List<Schedule>> schedulesByDate = filteredSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        // Сортировать сгруппированные сеансы по дате
        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();



        List<Hall> halls = hallService.findAll();
        List<Hall> filteredHalls = halls.stream().sorted(Comparator.comparing(Hall::getNumber))
                .toList();


        model.addAttribute("cinema", cinema.orElseThrow());
        model.addAttribute("schedulesByDate", sortedSchedulesByDate);
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("halls", filteredHalls);
        model.addAttribute("images", cinema.get().getImages());

        return "users-part/cinemas/cinema-info";
    }



    @GetMapping("/hall/{hallId}")
    public String hallSchedule(@PathVariable long hallId, Model model) {
        Optional<Hall> hall = hallService.findById(hallId);
        hall.ifPresent(value -> model.addAttribute("hall", value));

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Найти все расписания на сегодня для данного зала
        List<Schedule> schedules = scheduleService.findByHallIdAndDateRange(hallId, today, today);

        // Отфильтровать сеансы, которые начинаются сегодня и не прошли по времени
        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> schedule.getDate().isEqual(today) && schedule.getTime().isAfter(currentTime))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();

        // Группировать сеансы по дате
        Map<LocalDate, List<Schedule>> schedulesByDate = filteredSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        // Сортировать сгруппированные сеансы по дате
        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        model.addAttribute("schedulesByDate", sortedSchedulesByDate);
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("images", hall.get().getImages());

        return "users-part/cinemas/halls/hall-info";
    }


}
