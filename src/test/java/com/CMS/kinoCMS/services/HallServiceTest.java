package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.HallRepository;
import com.CMS.kinoCMS.admin.services.HallService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private HallService hallService;

    @Test
    public void shouldSaveHall() {
        Hall hall = getHall(1L);

        hallService.save(hall);

        Mockito.verify(hallRepository, Mockito.times(1)).save(hall);
    }

    @Test
    public void shouldReturnAllHalls() {
        List<Hall> halls = getHalls();

        Mockito.when(hallRepository.findAll()).thenReturn(halls);

        List<Hall> result = hallService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(halls.size(), result.size());
        Assertions.assertEquals(halls, result);
    }

    @Test
    public void shouldReturnHallById() {
        Long id = 1L;
        Hall hall = getHall(id);

        Mockito.when(hallRepository.findById(id)).thenReturn(Optional.of(hall));

        Optional<Hall> result = hallService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(hall, result.get());
    }

    @Test
    public void shouldReturnAllHallsByCinemaId() {
        Long cinemaId = 1L;
        List<Hall> halls = getHalls();

        Mockito.when(hallRepository.findAllByCinemaId(cinemaId)).thenReturn(halls);

        List<Hall> result = hallService.findAllByCinemaId(cinemaId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(halls.size(), result.size());
        Assertions.assertEquals(halls, result);
    }

    @Test
    public void shouldDeleteHallByOptional() {
        Hall hall = getHall(1L);
        Optional<Hall> optionalHall = Optional.of(hall);

        hallService.delete(optionalHall);

        Mockito.verify(hallRepository, Mockito.times(1)).delete(hall);
    }

    @Test
    public void shouldDeleteHall() {
        Hall hall = getHall(1L);

        hallService.delete(hall);

        Mockito.verify(hallRepository, Mockito.times(1)).delete(hall);
    }

    private Hall getHall(Long id) {
        Hall hall = new Hall();
        hall.setId(id);
        hall.setNumber(Math.toIntExact(id));
        hall.setTitleSEO("Seo of hall number " + id);
        return hall;
    }

    private List<Hall> getHalls() {
        Hall firstHall = getHall(1L);
        Hall secondHall = getHall(2L);
        return List.of(firstHall, secondHall);
    }
}
