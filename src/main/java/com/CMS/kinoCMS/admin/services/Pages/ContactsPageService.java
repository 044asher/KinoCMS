package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;
import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.repositories.Pages.ContactsPageRepository;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FileUploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
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
        log.info("Start ContactsPageService - findAll");
        List<Contact> contacts = contactsPageRepository.findAll();
        log.info("Successfully executed ContactsPageService - findAll");
        return contacts;
    }

    public Optional<Contact> findById(long id) {
        log.info("Start ContactsPageService - findById with id: {}", id);
        Optional<Contact> contact = contactsPageRepository.findById(id);
        log.info("Successfully executed ContactsPageService - findById with id: {}", id);
        return contact;
    }

    public void save(Contact contactPage) {
        log.info("Start ContactsPageService - save for contact: {}", contactPage.getName());
        contactsPageRepository.save(contactPage);
        log.info("Successfully executed ContactsPageService - save for contact: {}", contactPage.getName());
    }

    public void updateContactPage(Long id, Contact contact) {
        log.info("Start ContactsPageService - updateContactPage with id: {}", id);
        Contact existingContact = findById(id).orElseThrow(() -> new RuntimeException("Contact Not Found"));
        existingContact.setTitleSEO(contact.getTitleSEO());
        existingContact.setUrlSEO(contact.getUrlSEO());
        existingContact.setKeywordsSEO(contact.getKeywordsSEO());
        existingContact.setDescriptionSEO(contact.getDescriptionSEO());
        save(existingContact);
        log.info("Successfully executed ContactsPageService - updateContactPage with id: {}", id);
    }

    public void updateCinemaPage(Long id, CinemaUpdateDto cinemaUpdateDto, MultipartFile logoFile) throws IOException {
        log.info("Start ContactsPageService - updateCinemaPage with id: {}", id);
        Cinema existingCinema = cinemaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema Not Found"));

        if (logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileUploadService.uploadFile(logoFile);
            log.info("Uploaded new logo file: {}", fileName);
            existingCinema.setLogoName(fileName);
        }

        existingCinema.setAddress(cinemaUpdateDto.address());
        existingCinema.setXCoordinate(cinemaUpdateDto.xCoordinate());
        existingCinema.setYCoordinate(cinemaUpdateDto.yCoordinate());
        cinemaService.save(existingCinema);
        log.info("Successfully executed ContactsPageService - updateCinemaPage with id: {}", id);
    }
}
