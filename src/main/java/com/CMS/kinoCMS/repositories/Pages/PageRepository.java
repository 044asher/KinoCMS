package com.CMS.kinoCMS.repositories.Pages;

import com.CMS.kinoCMS.models.Pages.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
}
