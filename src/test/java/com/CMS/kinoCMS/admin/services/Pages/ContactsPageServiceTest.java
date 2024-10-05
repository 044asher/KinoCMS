package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.models.Pages.Contact;
import com.CMS.kinoCMS.repositories.Pages.ContactsPageRepository;
import com.CMS.kinoCMS.services.CinemaService;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.Pages.ContactsPageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactsPageServiceTest {

    @Mock
    private ContactsPageRepository contactsPageRepository;

    @Mock
    private CinemaService cinemaService;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private ContactsPageService contactsPageService;

    @Test
    void testFindAll() {
        List<Contact> contacts = List.of(new Contact(), new Contact());
        when(contactsPageRepository.findAll()).thenReturn(contacts);

        List<Contact> result = contactsPageService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(contactsPageRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Contact contact = new Contact();
        when(contactsPageRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactsPageService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(contact, result.get());
        verify(contactsPageRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Contact contact = new Contact();

        contactsPageService.save(contact);

        verify(contactsPageRepository, times(1)).save(contact);
    }

    @Test
    void testUpdateContactPage() {
        Contact contact = new Contact();
        contact.setTitleSEO("Title");
        contact.setUrlSEO("URL");
        contact.setKeywordsSEO("Keywords");
        contact.setDescriptionSEO("Description");

        Contact existingContact = new Contact();
        when(contactsPageRepository.findById(1L)).thenReturn(Optional.of(existingContact));

        contactsPageService.updateContactPage(1L, contact);

        assertEquals("Title", existingContact.getTitleSEO());
        assertEquals("URL", existingContact.getUrlSEO());
        assertEquals("Keywords", existingContact.getKeywordsSEO());
        assertEquals("Description", existingContact.getDescriptionSEO());
        verify(contactsPageRepository, times(1)).findById(1L);
        verify(contactsPageRepository, times(1)).save(existingContact);
    }

    @Test
    void testUpdateCinemaPage() throws IOException {
        Cinema cinema = new Cinema();
        when(cinemaService.findById(1L)).thenReturn(Optional.of(cinema));

        CinemaUpdateDto cinemaUpdateDto = new CinemaUpdateDto("New Address", 10.0, 20.0);

        MultipartFile logoFile = mock(MultipartFile.class);
        when(logoFile.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadFile(logoFile)).thenReturn("newLogo.png");

        contactsPageService.updateCinemaPage(1L, cinemaUpdateDto, logoFile);

        assertEquals("New Address", cinema.getAddress());
        assertEquals(10.0, cinema.getXCoordinate());
        assertEquals(20.0, cinema.getYCoordinate());
        assertEquals("newLogo.png", cinema.getLogoName());
        verify(cinemaService, times(1)).findById(1L);
        verify(fileUploadService, times(1)).uploadFile(logoFile);
        verify(cinemaService, times(1)).save(cinema);
    }
}
