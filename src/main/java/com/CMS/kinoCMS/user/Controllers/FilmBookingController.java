package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/film-booking")
public class FilmBookingController {

    private final ScheduleService scheduleService;

    @Autowired
    public FilmBookingController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{scheduleId}")
    public String getBookingPage(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.findById(scheduleId).orElseThrow();

        model.addAttribute("schedule", schedule);
        model.addAttribute("film", schedule.getFilm());
        model.addAttribute("hall", schedule.getHall());
        return "users-part/booking/film-booking";
    }
}
