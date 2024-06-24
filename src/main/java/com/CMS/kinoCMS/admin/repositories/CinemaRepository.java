package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}
