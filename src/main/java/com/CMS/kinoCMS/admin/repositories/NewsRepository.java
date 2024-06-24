package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
