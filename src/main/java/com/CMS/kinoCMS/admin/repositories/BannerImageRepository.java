package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Banners.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, Long> {
}
