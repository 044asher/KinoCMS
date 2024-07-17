package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findByCinemaIdAndDateRange(Long cinemaId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByCinemaIdAndDateBetween(cinemaId, startDate, endDate);
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public List<Schedule> findByCinemaId(long cinemaId) {
        return scheduleRepository.findByCinemaId(cinemaId);
    }

    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public List<Schedule> findByHallIdAndDateRange(long hallId, LocalDate today, LocalDate endDate) {
        return scheduleRepository.findByHallIdAndDateBetween(hallId, today, endDate);
    }

    public List<Schedule> findByFilmIdAndDateRange(long filmId, LocalDate today, LocalDate endDate) {
        return scheduleRepository.findByFilmIdAndDateBetween(filmId, today, endDate);
    }

    public List<Schedule> getFilteredSchedules(Long cinemaId, Long filmId, Long hallId, String filmType) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        List<Schedule> schedules = findByCinemaIdAndDateRange(cinemaId, today, oneWeekLater);

        return schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> filmId == null || schedule.getFilm().getId().equals(filmId))
                .filter(schedule -> hallId == null || schedule.getHall().getId().equals(hallId))
                .filter(schedule -> filmType == null || schedule.getFilm().getTypes().contains(filmType))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();
    }

    public Map<LocalDate, List<Schedule>> groupSchedulesByDate(List<Schedule> schedules) {
        return schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));
    }


}
