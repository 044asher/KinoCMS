package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(String role);
    long countByGender(String gender);
}
