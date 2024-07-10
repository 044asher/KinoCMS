package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
}
