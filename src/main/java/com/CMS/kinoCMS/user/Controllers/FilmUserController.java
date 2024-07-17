package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FilmService;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/film")
public class FilmUserController {
    private final FilmService filmService;
    private final CinemaService cinemaService;
    private final ScheduleService scheduleService;

    @Autowired
    public FilmUserController(FilmService filmService, CinemaService cinemaService, ScheduleService scheduleService) {
        this.filmService = filmService;
        this.cinemaService = cinemaService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{id}")
    public String film(@PathVariable long id, Model model) {
        Film film = filmService.findById(id).orElseThrow();
        List<Cinema> cinemas = cinemaService.findAll();

        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        List<Schedule> schedules = scheduleService.findByFilmIdAndDateRange(id, today, oneWeekLater);

        // Фильтрация расписаний на текущий день и последующие дни
        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .sorted(Comparator.comparing(Schedule::getDate).thenComparing(Schedule::getTime))
                .toList();

        // Группировка по кинотеатрам
        Map<Cinema, List<Schedule>> schedulesByCinema = filteredSchedules.stream()
                .collect(Collectors.groupingBy(Schedule::getCinema));

        // Сортировка расписаний внутри каждой группы кинотеатров
        Map<Cinema, List<Schedule>> sortedSchedulesByCinema = new LinkedHashMap<>();
        schedulesByCinema.forEach((cinema, scheduleList) -> {
            List<Schedule> sortedScheduleList = scheduleList.stream()
                    .sorted(Comparator.comparing(Schedule::getDate).thenComparing(Schedule::getTime))
                    .collect(Collectors.toList());
            sortedSchedulesByCinema.put(cinema, sortedScheduleList);
        });

        model.addAttribute("cinemas", cinemas);
        model.addAttribute("schedulesByCinema", sortedSchedulesByCinema);
        model.addAttribute("film", film);

        String embedLink = film.getLink().replace("watch?v=", "embed/");
        model.addAttribute("embedLink", embedLink);

        return "users-part/films/film";
    }
}
