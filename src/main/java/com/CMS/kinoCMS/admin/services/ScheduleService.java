package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.repositories.ScheduleRepository;
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
        log.info("Finding schedules for cinemaId: {}, between dates: {} and {}", cinemaId, startDate, endDate);
        return scheduleRepository.findByCinemaIdAndDateBetween(cinemaId, startDate, endDate);
    }

    public void save(Schedule schedule) {
        log.info("Saving schedule: {}", schedule);
        scheduleRepository.save(schedule);
    }

    public List<Schedule> findByCinemaId(long cinemaId) {
        log.info("Finding schedules for cinemaId: {}", cinemaId);
        return scheduleRepository.findByCinemaId(cinemaId);
    }

    public Optional<Schedule> findById(Long scheduleId) {
        log.info("Finding schedule by id: {}", scheduleId);
        return scheduleRepository.findById(scheduleId);
    }

    public List<Schedule> findByFilmIdAndDateRange(long filmId, LocalDate today, LocalDate endDate) {
        log.info("Finding schedules for filmId: {}, between dates: {} and {}", filmId, today, endDate);
        return scheduleRepository.findByFilmIdAndDateBetween(filmId, today, endDate);
    }

    public List<Schedule> getFilteredSchedules(Long cinemaId, Long filmId, Long hallId, String filmType) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        log.info("Filtering schedules for cinemaId: {}, filmId: {}, hallId: {}, filmType: {}", cinemaId, filmId, hallId, filmType);
        List<Schedule> schedules = findByCinemaIdAndDateRange(cinemaId, today, oneWeekLater);

        return schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> filmId == null || (schedule.getFilm() != null && schedule.getFilm().getId().equals(filmId)))
                .filter(schedule -> hallId == null || (schedule.getHall() != null && schedule.getHall().getId().equals(hallId)))
                .filter(schedule -> filmType == null || (schedule.getFilm() != null && schedule.getFilm().getTypes().contains(filmType)))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();
    }

    public Map<LocalDate, List<Schedule>> groupSchedulesByDate(List<Schedule> schedules) {
        log.info("Grouping schedules by date");
        return schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate));
    }

    public List<Schedule> getFilteredSchedulesHalls(Long hallId) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        log.info("Filtering schedules for hallId: {}", hallId);
        List<Schedule> schedules = scheduleRepository.findByHallIdAndDateBetween(hallId, today, oneWeekLater);

        return schedules.stream()
                .filter(schedule -> !schedule.getDate().isEqual(today) || schedule.getTime().isAfter(currentTime))
                .filter(schedule -> schedule.getHall().getId().equals(hallId))
                .sorted(Comparator.comparing(Schedule::getTime))
                .toList();
    }
}
