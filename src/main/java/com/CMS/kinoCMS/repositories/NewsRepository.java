package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByNotActive(boolean notActive);
}
