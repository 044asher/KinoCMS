package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
}
