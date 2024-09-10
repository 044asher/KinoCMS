package com.CMS.kinoCMS.admin.repositories.Pages;

import com.CMS.kinoCMS.admin.models.Pages.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findAllByIsDefault(Boolean isDefault);
    Optional<Page> findByName(String name);
    boolean existsByName(String name);

}
