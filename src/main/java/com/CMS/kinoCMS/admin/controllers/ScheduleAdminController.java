package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FilmService;
import com.CMS.kinoCMS.admin.services.HallService;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/schedules")
public class ScheduleAdminController {
    private final ScheduleService scheduleService;
    private final CinemaService cinemaService;
    private final FilmService filmService;
    private final HallService hallService;

    @Autowired
    public ScheduleAdminController(ScheduleService scheduleService, CinemaService cinemaService, FilmService filmService, HallService hallService) {
        this.scheduleService = scheduleService;
        this.cinemaService = cinemaService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping
    public String cinemas(Model model) {
        List<Cinema> cinemaList = cinemaService.findAll();
        model.addAttribute("cinemaList", cinemaList);
        return "schedules/cinemas-list";
    }

    @GetMapping("/{id}")
    public String schedules(@PathVariable long id, Model model) {
        Optional<Cinema> cinema = cinemaService.findById(id);
        List<Schedule> schedules = scheduleService.findByCinemaId(id);
        List<Schedule> sortedSchedules = schedules.stream().sorted(Comparator.comparing(Schedule::getDate))
                .toList();

        List<Film> films = filmService.findAll();
        List<Hall> halls = hallService.findAll();
        model.addAttribute("cinema", cinema.get());
        model.addAttribute("schedules", sortedSchedules);
        model.addAttribute("films", films);
        model.addAttribute("halls", halls);
        model.addAttribute("newSchedule", new Schedule());
        return "/schedules/schedules";
    }

    @PostMapping("/{id}")
    public String addSchedule(@PathVariable long id, @Valid @ModelAttribute("newSchedule") Schedule newSchedule, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return schedules(id, model);
        }
        newSchedule.setCinema(cinemaService.findById(id).get());
        scheduleService.save(newSchedule);
        return "redirect:/admin/schedules/" + id;
    }

    @PostMapping("/{cinemaId}/{scheduleId}/delete")
    public String deleteSchedule(@PathVariable long cinemaId, @PathVariable long scheduleId) {
        scheduleService.deleteById(scheduleId);
        return "redirect:/admin/schedules/" + cinemaId + "#schedules";
    }
}
