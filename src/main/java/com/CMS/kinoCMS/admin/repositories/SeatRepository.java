package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
