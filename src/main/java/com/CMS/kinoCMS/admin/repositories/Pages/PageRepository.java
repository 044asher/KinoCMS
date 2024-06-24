package com.CMS.kinoCMS.admin.repositories.Pages;

import com.CMS.kinoCMS.admin.models.Pages.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findAllByIsDefault(Boolean isDefault);
}
