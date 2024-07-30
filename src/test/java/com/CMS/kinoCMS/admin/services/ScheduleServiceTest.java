package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.repositories.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void testFindByCinemaIdAndDateRange() {
        Long cinemaId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        List<Schedule> schedules = List.of(new Schedule(), new Schedule());

        when(scheduleRepository.findByCinemaIdAndDateBetween(cinemaId, startDate, endDate)).thenReturn(schedules);

        List<Schedule> result = scheduleService.findByCinemaIdAndDateRange(cinemaId, startDate, endDate);
        assertEquals(schedules, result);
        verify(scheduleRepository, times(1)).findByCinemaIdAndDateBetween(cinemaId, startDate, endDate);
    }

    @Test
    public void testSave() {
        Schedule schedule = new Schedule();
        scheduleService.save(schedule);
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    public void testFindByCinemaId() {
        long cinemaId = 1L;
        List<Schedule> schedules = List.of(new Schedule(), new Schedule());

        when(scheduleRepository.findByCinemaId(cinemaId)).thenReturn(schedules);

        List<Schedule> result = scheduleService.findByCinemaId(cinemaId);
        assertEquals(schedules, result);
        verify(scheduleRepository, times(1)).findByCinemaId(cinemaId);
    }

    @Test
    public void testFindById() {
        Long scheduleId = 1L;
        Schedule schedule = new Schedule();

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        Optional<Schedule> result = scheduleService.findById(scheduleId);
        assertTrue(result.isPresent());
        assertEquals(schedule, result.get());
        verify(scheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    public void testFindByFilmIdAndDateRange() {
        long filmId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        List<Schedule> schedules = List.of(new Schedule(), new Schedule());

        when(scheduleRepository.findByFilmIdAndDateBetween(filmId, startDate, endDate)).thenReturn(schedules);

        List<Schedule> result = scheduleService.findByFilmIdAndDateRange(filmId, startDate, endDate);
        assertEquals(schedules, result);
        verify(scheduleRepository, times(1)).findByFilmIdAndDateBetween(filmId, startDate, endDate);
    }

    @Test
    public void testGetFilteredSchedules() {
        Long cinemaId = 1L;
        Long filmId = 1L;
        Long hallId = 1L;
        String filmType = "3D";
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        Schedule schedule1 = new Schedule();
        schedule1.setDate(today);
        schedule1.setTime(currentTime.plusHours(1));
        Film film1 = new Film();
        film1.setId(filmId);
        film1.setTypes(filmType);
        schedule1.setFilm(film1);
        Hall hall1 = new Hall();
        hall1.setId(hallId);
        schedule1.setHall(hall1);

        Schedule schedule2 = new Schedule();
        schedule2.setDate(today.plusDays(1));
        schedule2.setTime(LocalTime.of(14, 0));
        Film film2 = new Film();
        film2.setId(filmId);
        film2.setTypes(filmType);
        schedule2.setFilm(film2);
        Hall hall2 = new Hall();
        hall2.setId(hallId);
        schedule2.setHall(hall2);

        List<Schedule> schedules = List.of(schedule1, schedule2);

        when(scheduleRepository.findByCinemaIdAndDateBetween(cinemaId, today, oneWeekLater)).thenReturn(schedules);

        List<Schedule> result = scheduleService.getFilteredSchedules(cinemaId, filmId, hallId, filmType);
        assertEquals(2, result.size());
    }

    @Test
    public void testGroupSchedulesByDate() {
        Schedule schedule1 = new Schedule();
        schedule1.setDate(LocalDate.now());
        Schedule schedule2 = new Schedule();
        schedule2.setDate(LocalDate.now().plusDays(1));
        List<Schedule> schedules = List.of(schedule1, schedule2);

        Map<LocalDate, List<Schedule>> result = scheduleService.groupSchedulesByDate(schedules);
        assertEquals(2, result.size());
        assertEquals(1, result.get(LocalDate.now()).size());
        assertEquals(1, result.get(LocalDate.now().plusDays(1)).size());
    }

    @Test
    public void testGetFilteredSchedulesHalls() {
        Long hallId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);
        LocalTime currentTime = LocalTime.now();

        Schedule schedule1 = new Schedule();
        schedule1.setDate(today);
        schedule1.setTime(currentTime.plusHours(1));
        schedule1.setHall(new Hall());
        schedule1.getHall().setId(hallId);
        Schedule schedule2 = new Schedule();
        schedule2.setDate(today.plusDays(1));
        schedule2.setTime(currentTime.plusHours(1));
        schedule2.setHall(new Hall());
        schedule2.getHall().setId(hallId);
        List<Schedule> schedules = List.of(schedule1, schedule2);

        when(scheduleRepository.findByHallIdAndDateBetween(hallId, today, oneWeekLater)).thenReturn(schedules);

        List<Schedule> result = scheduleService.getFilteredSchedulesHalls(hallId);
        assertEquals(2, result.size());
        assertTrue(result.contains(schedule1));
        assertTrue(result.contains(schedule2));
        verify(scheduleRepository, times(1)).findByHallIdAndDateBetween(hallId, today, oneWeekLater);
    }

    @Test
    public void testDeleteById(){
        Long id = 1L;
        scheduleRepository.deleteById(id);
        verify(scheduleRepository, times(1)).deleteById(id);
    }
}

