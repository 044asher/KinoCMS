package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByNotActive(boolean notActive);
}
