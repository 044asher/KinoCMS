package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.repositories.CinemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @Mock
    private HallService hallService;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private CinemaService cinemaService;

    @Test
    void testFindAll() {
        List<Cinema> cinemas = List.of(new Cinema(), new Cinema());
        when(cinemaRepository.findAll()).thenReturn(cinemas);

        List<Cinema> result = cinemaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cinemaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Cinema cinema = new Cinema();
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(cinema));

        Optional<Cinema> result = cinemaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(cinema, result.get());
        verify(cinemaRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Cinema cinema = new Cinema();

        cinemaService.save(cinema);

        verify(cinemaRepository, times(1)).save(cinema);
    }

    @Test
    void testDelete() {
        Cinema cinema = new Cinema();

        cinemaService.delete(cinema);

        verify(cinemaRepository, times(1)).delete(cinema);
    }

    @Test
    void testSaveCinema() throws IOException {
        Cinema cinema = new Cinema();
        MultipartFile logo = mock(MultipartFile.class);
        MultipartFile banner = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        List<Integer> hallNumber = List.of(1, 2);
        List<String> hallDescription = List.of("Hall 1", "Hall 2");
        List<MultipartFile> hallScheme = List.of(mock(MultipartFile.class), mock(MultipartFile.class));
        List<MultipartFile> hallBanner = List.of(mock(MultipartFile.class), mock(MultipartFile.class));
        List<String> urlSeo = List.of("url1", "url2");
        List<String> titleSeo = List.of("title1", "title2");
        List<String> keywordsSeo = List.of("keywords1", "keywords2");
        List<String> descriptionSeo = List.of("description1", "description2");

        when(logo.isEmpty()).thenReturn(false);
        when(banner.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(logo)).thenReturn("logo.jpg");
        when(fileUploadService.uploadFile(banner)).thenReturn("banner.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        cinemaService.saveCinema(cinema, logo, banner, additionalFiles, hallNumber, hallDescription, hallScheme, hallBanner, urlSeo, titleSeo, keywordsSeo, descriptionSeo);

        assertEquals("logo.jpg", cinema.getLogoName());
        assertEquals("banner.jpg", cinema.getBannerName());
        assertEquals(2, cinema.getImages().size());
        verify(cinemaRepository, times(1)).save(cinema);
        verify(hallService, times(2)).save(any(Hall.class));
    }

    @Test
    void testUpdateCinema() throws IOException {
        Cinema existingCinema = new Cinema();
        existingCinema.setId(1L);
        Cinema updatedCinema = new Cinema();
        updatedCinema.setName("Updated Cinema");
        updatedCinema.setDescription("Updated Description");
        updatedCinema.setConditions("Updated Conditions");
        updatedCinema.setUrlSEO("updatedUrlSEO");
        updatedCinema.setTitleSEO("updatedTitleSEO");
        updatedCinema.setKeywordsSEO("updatedKeywordsSEO");
        updatedCinema.setDescriptionSEO("updatedDescriptionSEO");

        MultipartFile logo = mock(MultipartFile.class);
        MultipartFile banner = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};
        when(cinemaRepository.findById(1L)).thenReturn(Optional.of(existingCinema));
        when(logo.isEmpty()).thenReturn(false);
        when(banner.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(logo)).thenReturn("updatedLogo.jpg");
        when(fileUploadService.uploadFile(banner)).thenReturn("updatedBanner.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("image1.jpg", "image2.jpg"));

        cinemaService.updateCinema(1L, updatedCinema, logo, banner, additionalFiles);

        assertEquals("Updated Cinema", existingCinema.getName());
        assertEquals("Updated Description", existingCinema.getDescription());
        assertEquals("Updated Conditions", existingCinema.getConditions());
        assertEquals("updatedLogo.jpg", existingCinema.getLogoName());
        assertEquals("updatedBanner.jpg", existingCinema.getBannerName());
        assertEquals(2, existingCinema.getImages().size());
        assertEquals("updatedUrlSEO", existingCinema.getUrlSEO());
        assertEquals("updatedTitleSEO", existingCinema.getTitleSEO());
        assertEquals("updatedKeywordsSEO", existingCinema.getKeywordsSEO());
        assertEquals("updatedDescriptionSEO", existingCinema.getDescriptionSEO());
        verify(cinemaRepository, times(1)).findById(1L);
        verify(cinemaRepository, times(1)).save(existingCinema);
    }
}
