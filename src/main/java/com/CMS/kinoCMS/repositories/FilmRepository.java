package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByDateBeforeOrDate(LocalDate date, LocalDate now);
    List<Film> findByDateAfterOrDateIsNull(LocalDate date);
}
