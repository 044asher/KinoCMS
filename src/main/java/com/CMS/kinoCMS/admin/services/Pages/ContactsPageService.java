package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.repositories.Pages.ContactsPageRepository;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ContactsPageService {
    private final ContactsPageRepository contactsPageRepository;
    private final CinemaService cinemaService;
    private final FileUploadService fileUploadService;

    @Autowired
    public ContactsPageService(ContactsPageRepository contactsPageRepository, CinemaService cinemaService, FileUploadService fileUploadService) {
        this.contactsPageRepository = contactsPageRepository;
        this.cinemaService = cinemaService;
        this.fileUploadService = fileUploadService;
    }

    public List<Contact> findAll() {
        return contactsPageRepository.findAll();
    }

    public Optional<Contact> findById(long id) {
        return contactsPageRepository.findById(id);
    }

    public void save(Contact contactPage) {
        contactsPageRepository.save(contactPage);
    }

    public void updateContactPage(Long id, Contact contact) {
        Contact existingContact = findById(id).orElseThrow(() -> new RuntimeException("Contact Not Found"));
        existingContact.setTitleSEO(contact.getTitleSEO());
        existingContact.setUrlSEO(contact.getUrlSEO());
        existingContact.setKeywordsSEO(contact.getKeywordsSEO());
        existingContact.setDescriptionSEO(contact.getDescriptionSEO());
        save(existingContact);
    }

    public void updateCinemaPage(Long id, CinemaUpdateDto cinemaUpdateDto, MultipartFile logoFile) throws IOException {
        Cinema existingCinema = cinemaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema Not Found"));

        if (logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileUploadService.uploadFile(logoFile);
            existingCinema.setLogoName(fileName);
        }

        existingCinema.setAddress(cinemaUpdateDto.getAddress());
        existingCinema.setXCoordinate(cinemaUpdateDto.getXCoordinate());
        existingCinema.setYCoordinate(cinemaUpdateDto.getYCoordinate());
        cinemaService.save(existingCinema);
    }
}
