package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
