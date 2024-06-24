package com.CMS.kinoCMS.admin.repositories.Pages;

import com.CMS.kinoCMS.admin.models.Pages.MainPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPageRepository extends JpaRepository<MainPage, Long> {
}
