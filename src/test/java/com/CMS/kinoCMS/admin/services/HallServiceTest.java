package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.HallRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private HallService hallService;

    @Test
    public void testSave() {
        Hall hall = new Hall();
        hall.setId(1L);

        hallService.save(hall);

        verify(hallRepository, times(1)).save(hall);
    }

    @Test
    public void testFindAll() {
        List<Hall> halls = List.of(new Hall(), new Hall());
        when(hallRepository.findAll()).thenReturn(halls);

        List<Hall> result = hallService.findAll();

        assertEquals(2, result.size());
        verify(hallRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Hall hall = new Hall();
        hall.setId(1L);
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));

        Optional<Hall> result = hallService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(hallRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_HallNotFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Hall> result = hallService.findById(1L);

        assertFalse(result.isPresent());
        verify(hallRepository, times(1)).findById(1L);
    }


    @Test
    public void testFindAllByCinemaId() {
        List<Hall> halls = List.of(new Hall(), new Hall());
        when(hallRepository.findAllByCinemaId(1L)).thenReturn(halls);

        List<Hall> result = hallService.findAllByCinemaId(1L);

        assertEquals(2, result.size());
        verify(hallRepository, times(1)).findAllByCinemaId(1L);
    }

    @Test
    public void testDeleteByOptional() {
        Hall hall = new Hall();
        hall.setId(1L);
        Optional<Hall> optionalHall = Optional.of(hall);

        hallService.delete(optionalHall);

        verify(hallRepository, times(1)).delete(hall);
    }

    @Test
    public void testDelete() {
        Hall hall = new Hall();
        hall.setId(1L);

        hallService.delete(hall);

        verify(hallRepository, times(1)).delete(hall);
    }

    @Test
    public void testDelete_HallNotPresent() {
        Optional<Hall> hall = Optional.empty();

        hallService.delete(hall);

        verify(hallRepository, times(0)).delete(any(Hall.class));
    }

}
