package com.CMS.kinoCMS.repositories.Pages;

import com.CMS.kinoCMS.models.Pages.MainPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPageRepository extends JpaRepository<MainPage, Long> {
}
