package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        List<Schedule> schedules = scheduleService.findByCinemaIdAndDateRange(id, today, oneWeekLater);

        // Фильтрация расписаний на текущий день и последующие дни
        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> filmId == null || schedule.getFilm().getId().equals(filmId))
                .filter(schedule -> hallId == null || schedule.getHall().getId().equals(hallId))
                .filter(schedule -> filmType == null || schedule.getFilm().getTypes().contains(filmType))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();

        // Группировка по датам
        Map<LocalDate, List<Schedule>> schedulesByDate = filteredSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));

        // Сортировка по датам
        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

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
