package com.CMS.kinoCMS.repositories.Pages;

import com.CMS.kinoCMS.models.Pages.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactsPageRepository extends JpaRepository<Contact, Long> {
}
