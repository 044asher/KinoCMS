package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.Banners.BannersAndSliders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannersAndSlidersRepository extends JpaRepository<BannersAndSliders, Long> {
}
