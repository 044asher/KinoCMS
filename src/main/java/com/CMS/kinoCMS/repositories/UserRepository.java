package com.CMS.kinoCMS.repositories;

import com.CMS.kinoCMS.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(String role);
    long countByGender(String gender);
    long countByCityId(long cityId);

    Page<User> findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(String username, String email, String firstName, String lastName, String phoneNumber, Pageable pageable);
}
