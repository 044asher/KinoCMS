package com.CMS.kinoCMS.services.pages;

import com.CMS.kinoCMS.models.Pages.Contact;
import com.CMS.kinoCMS.repositories.Pages.ContactsPageRepository;
import com.CMS.kinoCMS.services.Pages.ContactsPageService;
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
public class ContactsPageServiceTest {

    @Mock
    private ContactsPageRepository contactsPageRepository;

    @InjectMocks
    private ContactsPageService contactsPageService;

    @Test
    public void shouldReturnAllContacts() {
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        List<Contact> contactList = List.of(contact1, contact2);

        Mockito.when(contactsPageRepository.findAll()).thenReturn(contactList);

        List<Contact> result = contactsPageService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(contactList, result);
    }

    @Test
    public void shouldReturnContactById() {
        long id = 1L;
        Contact contact = new Contact();
        contact.setId(id);

        Mockito.when(contactsPageRepository.findById(id)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactsPageService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(contact, result.get());
    }

    @Test
    public void shouldSaveContact() {
        Contact contact = new Contact();
        contact.setId(1L);

        contactsPageService.save(contact);

        Mockito.verify(contactsPageRepository, Mockito.times(1)).save(contact);
    }
}
