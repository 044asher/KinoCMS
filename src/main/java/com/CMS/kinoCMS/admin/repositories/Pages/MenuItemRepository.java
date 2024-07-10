package com.CMS.kinoCMS.admin.repositories.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MenuItem;
import com.CMS.kinoCMS.admin.models.Pages.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByPage(Page page);
}
