package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.repositories.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private FilmService filmService;

    private Film film;

    @BeforeEach
    public void setup() {
        film = new Film();
        film.setId(1L);
        film.setName("Test Film");
    }

    @Test
    public void testGetCurrentFilms() {
        LocalDate today = LocalDate.now();
        when(filmRepository.findByDateBeforeOrDate(today, today)).thenReturn(List.of(film));

        List<Film> currentFilms = filmService.getCurrentFilms();

        assertEquals(1, currentFilms.size());
        assertEquals("Test Film", currentFilms.getFirst().getName());
        verify(filmRepository, times(1)).findByDateBeforeOrDate(today, today);
    }

    @Test
    public void testGetUpcomingFilms() {
        LocalDate today = LocalDate.now();
        when(filmRepository.findByDateAfterOrDateIsNull(today)).thenReturn(List.of(film));

        List<Film> upcomingFilms = filmService.getUpcomingFilms();

        assertEquals(1, upcomingFilms.size());
        assertEquals("Test Film", upcomingFilms.getFirst().getName());
        verify(filmRepository, times(1)).findByDateAfterOrDateIsNull(today);
    }

    @Test
    public void testGetPrePremieresFilms() {
        when(filmRepository.findByIsPrePremiere(true)).thenReturn(List.of(film));

        List<Film> prePremieresFilms = filmService.getPrePremieresFilms(true);

        assertEquals(1, prePremieresFilms.size());
        assertEquals("Test Film", prePremieresFilms.getFirst().getName());
        verify(filmRepository, times(1)).findByIsPrePremiere(true);
    }

    @Test
    public void testSave() {
        filmService.save(film);

        verify(filmRepository, times(1)).save(film);
    }

    @Test
    public void testFindById() {
        when(filmRepository.findById(1L)).thenReturn(Optional.of(film));

        Optional<Film> foundFilm = filmService.findById(1L);

        assertTrue(foundFilm.isPresent());
        assertEquals("Test Film", foundFilm.get().getName());
        verify(filmRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() {
        when(filmRepository.findAll()).thenReturn(List.of(film));

        List<Film> allFilms = filmService.findAll();

        assertEquals(1, allFilms.size());
        assertEquals("Test Film", allFilms.getFirst().getName());
        verify(filmRepository, times(1)).findAll();
    }

    @Test
    public void testProcessFilmData() throws IOException {
        MultipartFile mainFile = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        List<String> filmTypes = List.of("Type1", "Type2");

        when(mainFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(mainFile)).thenReturn("mainFile.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg", "add2.jpg"));

        filmService.processFilmData(film, mainFile, additionalFiles, filmTypes, 2023, "Country", "Musician",
                List.of("Producer"), "Director", List.of("Writer"), List.of("Genre"), 18, 120);

        assertEquals("mainFile.jpg", film.getMainImage());
        assertEquals(2, film.getImages().size());
        assertEquals("Type1,Type2", film.getTypes());
        assertEquals(2023, film.getYear());
        assertEquals("Country", film.getCountry());
        assertEquals("Musician", film.getMusician());
        assertEquals("Producer", film.getProducer().getFirst());
        assertEquals("Director", film.getDirector());
        assertEquals("Writer", film.getWriter().getFirst());
        assertEquals("Genre", film.getGenre().getFirst());
        assertEquals(18, film.getAge());
        assertEquals(120, film.getTime());

        verify(fileUploadService, times(1)).uploadFile(mainFile);
        verify(fileUploadService, times(1)).uploadAdditionalFiles(additionalFiles);
        verify(filmRepository, times(1)).save(film);
    }

    @Test
    public void testUpdateFilm() throws IOException {
        MultipartFile mainFile = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        List<String> filmTypes = List.of("Type1", "Type2");

        Film updatedFilm = new Film();
        updatedFilm.setName("Updated Film");

        when(filmRepository.findById(1L)).thenReturn(Optional.of(film));
        when(mainFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(mainFile)).thenReturn("mainFile.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg", "add2.jpg"));

        filmService.updateFilm(1L, updatedFilm, mainFile, additionalFiles, filmTypes, 2023, "Country", "Musician",
                List.of("Producer"), "Director", List.of("Writer"), List.of("Genre"), 18, 120);

        assertEquals("Updated Film", film.getName());
        assertEquals("mainFile.jpg", film.getMainImage());
        assertEquals(2, film.getImages().size());
        assertEquals("Type1,Type2", film.getTypes());
        assertEquals(2023, film.getYear());
        assertEquals("Country", film.getCountry());
        assertEquals("Musician", film.getMusician());
        assertEquals("Producer", film.getProducer().getFirst());
        assertEquals("Director", film.getDirector());
        assertEquals("Writer", film.getWriter().getFirst());
        assertEquals("Genre", film.getGenre().getFirst());
        assertEquals(18, film.getAge());
        assertEquals(120, film.getTime());

        verify(fileUploadService, times(1)).uploadFile(mainFile);
        verify(fileUploadService, times(1)).uploadAdditionalFiles(additionalFiles);
        verify(filmRepository, times(1)).save(film);
    }

    @Test
    void testUpdateFilm_WithDate() throws IOException {
        // Arrange
        Film existingFilm = new Film();
        existingFilm.setDate(LocalDate.of(2022, 1, 1));
        Film updatedFilm = new Film();
        updatedFilm.setDate(LocalDate.of(2023, 1, 1));

        MultipartFile mainFile = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};
        List<String> filmTypes = List.of("Type1");

        when(filmRepository.findById(1L)).thenReturn(Optional.of(existingFilm));
        when(mainFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(mainFile)).thenReturn("mainFile.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg"));

        // Act
        filmService.updateFilm(1L, updatedFilm, mainFile, additionalFiles, filmTypes, 2023, "Country", "Musician",
                List.of("Producer"), "Director", List.of("Writer"), List.of("Genre"), 18, 120);

        // Assert
        assertEquals(LocalDate.of(2023, 1, 1), existingFilm.getDate());
        verify(fileUploadService, times(1)).uploadFile(mainFile);
        verify(fileUploadService, times(1)).uploadAdditionalFiles(additionalFiles);
        verify(filmRepository, times(1)).save(existingFilm);
    }


    @Test
    void testUpdateFilm_PrePremiereSetFalse() throws IOException {
        // Arrange
        Film existingFilm = new Film();
        existingFilm.setPrePremiere(true);
        Film updatedFilm = new Film();
        updatedFilm.setDate(LocalDate.of(2022, 1, 1)); // Date in the past

        MultipartFile mainFile = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};
        List<String> filmTypes = List.of("Type1");

        when(filmRepository.findById(1L)).thenReturn(Optional.of(existingFilm));
        when(mainFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(mainFile)).thenReturn("mainFile.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg"));

        // Act
        filmService.updateFilm(1L, updatedFilm, mainFile, additionalFiles, filmTypes, 2023, "Country", "Musician",
                List.of("Producer"), "Director", List.of("Writer"), List.of("Genre"), 18, 120);

        // Assert
        assertFalse(existingFilm.isPrePremiere());
        verify(fileUploadService, times(1)).uploadFile(mainFile);
        verify(fileUploadService, times(1)).uploadAdditionalFiles(additionalFiles);
        verify(filmRepository, times(1)).save(existingFilm);
    }

    @Test
    void testUpdateFilm_FilmTypesNull() throws IOException {
        Film existingFilm = new Film();
        Film updatedFilm = new Film();

        MultipartFile mainFile = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class)};

        when(filmRepository.findById(1L)).thenReturn(Optional.of(existingFilm));
        when(mainFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(mainFile)).thenReturn("mainFile.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg"));

        filmService.updateFilm(1L, updatedFilm, mainFile, additionalFiles, null, 2023, "Country", "Musician",
                List.of("Producer"), "Director", List.of("Writer"), List.of("Genre"), 18, 120);

        assertEquals("", existingFilm.getTypes());
        verify(fileUploadService, times(1)).uploadFile(mainFile);
        verify(fileUploadService, times(1)).uploadAdditionalFiles(additionalFiles);
        verify(filmRepository, times(1)).save(existingFilm);
    }

}
