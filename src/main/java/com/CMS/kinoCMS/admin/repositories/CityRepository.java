package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
