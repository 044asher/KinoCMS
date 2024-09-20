package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Schedule;
import com.CMS.kinoCMS.repositories.ScheduleRepository;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findByCinemaIdAndDateRange(Long cinemaId, LocalDate startDate, LocalDate endDate) {
        log.info("Start ScheduleService - findByCinemaIdAndDateRange with cinemaId: {}, startDate: {}, endDate: {}", cinemaId, startDate, endDate);
        List<Schedule> schedules = scheduleRepository.findByCinemaIdAndDateBetween(cinemaId, startDate, endDate);
        log.info("End ScheduleService - findByCinemaIdAndDateRange. Found {} schedules", schedules.size());
        return schedules;
    }

    public void save(Schedule schedule) {
        log.info("Start ScheduleService - save with schedule: {}", schedule);
        scheduleRepository.save(schedule);
        log.info("End ScheduleService - save. Schedule saved: {}", schedule);
    }

    public List<Schedule> findByCinemaId(long cinemaId) {
        log.info("Start ScheduleService - findByCinemaId with cinemaId: {}", cinemaId);
        List<Schedule> schedules = scheduleRepository.findByCinemaId(cinemaId);
        log.info("End ScheduleService - findByCinemaId. Found {} schedules", schedules.size());
        return schedules;
    }

    public Optional<Schedule> findById(Long scheduleId) {
        log.info("Start ScheduleService - findById with scheduleId: {}", scheduleId);
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isPresent()) {
            log.info("End ScheduleService - findById. Found schedule with id: {}", scheduleId);
        } else {
            log.warn("End ScheduleService - findById. No schedule found with id: {}", scheduleId);
        }
        return schedule;
    }

    public List<Schedule> findByFilmIdAndDateRange(long filmId, LocalDate today, LocalDate endDate) {
        log.info("Start ScheduleService - findByFilmIdAndDateRange with filmId: {}, startDate: {}, endDate: {}", filmId, today, endDate);
        List<Schedule> schedules = scheduleRepository.findByFilmIdAndDateBetween(filmId, today, endDate);
        log.info("End ScheduleService - findByFilmIdAndDateRange. Found {} schedules", schedules.size());
        return schedules;
    }

    public List<Schedule> getFilteredSchedules(Long cinemaId, Long filmId, Long hallId, String filmType) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        log.info("Start ScheduleService - getFilteredSchedules with cinemaId: {}, filmId: {}, hallId: {}, filmType: {}", cinemaId, filmId, hallId, filmType);
        List<Schedule> schedules = findByCinemaIdAndDateRange(cinemaId, today, oneWeekLater);

        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> filmId == null || (schedule.getFilm() != null && schedule.getFilm().getId().equals(filmId)))
                .filter(schedule -> hallId == null || (schedule.getHall() != null && schedule.getHall().getId().equals(hallId)))
                .filter(schedule -> filmType == null || (schedule.getFilm() != null && schedule.getFilm().getTypes().contains(filmType)))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();

        log.info("End ScheduleService - getFilteredSchedules. Filtered {} schedules", filteredSchedules.size());
        return filteredSchedules;
    }

    public Map<LocalDate, List<Schedule>> groupSchedulesByDate(List<Schedule> schedules) {
        log.info("Start ScheduleService - groupSchedulesByDate with {} schedules", schedules.size());
        Map<LocalDate, List<Schedule>> groupedSchedules = schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));
        log.info("End ScheduleService - groupSchedulesByDate. Grouped schedules into {} dates", groupedSchedules.size());
        return groupedSchedules;
    }

    public List<Schedule> getFilteredSchedulesHalls(Long hallId) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        log.info("Start ScheduleService - getFilteredSchedulesHalls with hallId: {}", hallId);
        List<Schedule> schedules = scheduleRepository.findByHallIdAndDateBetween(hallId, today, oneWeekLater);

        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> schedule.getHall().getId().equals(hallId))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();

        log.info("End ScheduleService - getFilteredSchedulesHalls. Filtered {} schedules", filteredSchedules.size());
        return filteredSchedules;
    }

    public void deleteById(long scheduleId) {
        log.info("Start ScheduleService - deleteById with scheduleId: {}", scheduleId);
        scheduleRepository.deleteById(scheduleId);
        log.info("End ScheduleService - deleteById. Deleted schedule with id: {}", scheduleId);
    }
}