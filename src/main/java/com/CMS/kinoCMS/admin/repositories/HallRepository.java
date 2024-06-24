package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findAllByCinemaId(long cinemaId);
}
