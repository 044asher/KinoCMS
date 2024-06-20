package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.repositories.CinemaRepository;
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
public class CinemaServiceTest {
    @Mock
    private CinemaRepository cinemaRepository;

    @InjectMocks
    private CinemaService cinemaService;

    @Test
    public void shouldReturnAllCinemas() {
        List<Cinema> cinemas = getCinemas();

        Mockito.when(cinemaRepository.findAll()).thenReturn(cinemas);

        List<Cinema> result = cinemaService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cinemas.size(), result.size());
        Assertions.assertEquals(cinemas, result);
    }

    @Test
    public void shouldSaveCinema() {
        Cinema cinema = getCinema(1L);

        cinemaService.save(cinema);

        Mockito.verify(cinemaRepository, Mockito.times(1)).save(cinema);
    }

    @Test
    public void shouldReturnCinemaById() {
        Long id = 1L;
        Cinema cinema = getCinema(id);

        Mockito.when(cinemaRepository.findById(id)).thenReturn(Optional.of(cinema));

        Optional<Cinema> result = cinemaService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(cinema, result.get());
    }

    @Test
    public void shouldDeleteCinema() {
        Cinema cinema = getCinema(1L);
        Optional<Cinema> optionalCinema = Optional.of(cinema);

        cinemaService.delete(optionalCinema);

        Mockito.verify(cinemaRepository, Mockito.times(1)).delete(cinema);
    }

    private Cinema getCinema(Long id) {
        Cinema cinema = new Cinema();
        cinema.setId(id);
        cinema.setName("Cinema " + id);
        cinema.setConditions("Conditions " + id);
        cinema.setDescription("Description " + id);
        cinema.setBannerName("img");
        cinema.setXCoordinate(2.5);
        cinema.setYCoordinate(2.5);
        cinema.setAddress("Address " + id);
        cinema.setTitleSEO("Title SEO");
        cinema.setDescriptionSEO("Description SEO");
        cinema.setUrlSEO("https://www.seo.com");
        cinema.setKeywordsSEO("Keywords SEO");
        return cinema;
    }

    private List<Cinema> getCinemas() {
        Cinema firstCinema = getCinema(1L);
        Cinema secondCinema = getCinema(2L);
        return List.of(firstCinema, secondCinema);
    }
}
