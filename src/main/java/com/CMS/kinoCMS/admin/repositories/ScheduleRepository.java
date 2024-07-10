package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCinemaIdAndDateBetween(Long cinemaId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByCinemaId(long cinemaId);

    List<Schedule> findByHallIdAndDateBetween(long hallId, LocalDate today, LocalDate endDate);
}
