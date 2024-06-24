package com.CMS.kinoCMS.admin.services.Pages;

import com.CMS.kinoCMS.admin.models.Pages.Contact;
import com.CMS.kinoCMS.admin.repositories.Pages.ContactsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactsPageService {
    private final ContactsPageRepository contactsPageRepository;

    @Autowired
    public ContactsPageService(ContactsPageRepository contactsPageRepository) {
        this.contactsPageRepository = contactsPageRepository;
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
}
