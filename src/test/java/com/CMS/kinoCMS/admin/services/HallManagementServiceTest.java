package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.Hall;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.HallManagementService;
import com.CMS.kinoCMS.services.HallService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HallManagementServiceTest {
    @Mock
    private HallService hallService;

    @Mock
    private CinemaService cinemaService;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private HallManagementService hallManagementService;

    private Hall hall;
    private Cinema cinema;

    @BeforeEach
    public void setup() {
        hall = new Hall();
        hall.setId(1L);
        hall.setNumber(1);
        hall.setDescription("Test Hall");

        cinema = new Cinema();
        cinema.setId(1L);
        cinema.setName("Test Cinema");
    }

    @Test
    public void testUpdateHall() throws IOException {
        MultipartFile logo = mock(MultipartFile.class);
        MultipartFile banner = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};

        when(hallService.findById(1L)).thenReturn(Optional.of(hall));
        when(logo.isEmpty()).thenReturn(false);
        when(banner.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(logo)).thenReturn("logo.jpg");
        when(fileUploadService.uploadFile(banner)).thenReturn("banner.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg", "add2.jpg"));

        hallManagementService.updateHall(1L, hall, logo, banner, additionalFiles);

        assertEquals("logo.jpg", hall.getScheme());
        assertEquals("banner.jpg", hall.getTopBanner());
        assertEquals(2, hall.getImages().size());

        verify(hallService, times(1)).save(hall);
    }

    @Test
    public void testAddHall() throws IOException {
        MultipartFile logo = mock(MultipartFile.class);
        MultipartFile banner = mock(MultipartFile.class);
        MultipartFile[] additionalFiles = {mock(MultipartFile.class), mock(MultipartFile.class)};

        when(cinemaService.findById(1L)).thenReturn(Optional.of(cinema));
        when(logo.isEmpty()).thenReturn(false);
        when(banner.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(logo)).thenReturn("logo.jpg");
        when(fileUploadService.uploadFile(banner)).thenReturn("banner.jpg");
        when(fileUploadService.uploadAdditionalFiles(additionalFiles)).thenReturn(List.of("add1.jpg", "add2.jpg"));

        hallManagementService.addHall(1L, hall, logo, banner, additionalFiles);

        assertEquals("logo.jpg", hall.getScheme());
        assertEquals("banner.jpg", hall.getTopBanner());
        assertEquals(2, hall.getImages().size());
        assertEquals(cinema, hall.getCinema());
        assertEquals(LocalDate.now(), hall.getCreationDate());

        verify(hallService, times(1)).save(hall);
    }

    @Test
    public void testUpdateHall_NotFound() {
        when(hallService.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> hallManagementService.updateHall(1L, hall, null, null, null));

        assertEquals("Hall with id 1 not found", exception.getMessage());
    }

    @Test
    public void testAddHall_CinemaNotFound() {
        when(cinemaService.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> hallManagementService.addHall(1L, hall, null, null, null));

        assertEquals("Invalid cinema Id: 1", exception.getMessage());
    }
}

