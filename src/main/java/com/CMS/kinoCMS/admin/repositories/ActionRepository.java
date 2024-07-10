package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByNotActive(boolean isActive);
}
