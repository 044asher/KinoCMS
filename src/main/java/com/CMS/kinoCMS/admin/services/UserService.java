package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> findAllUsers(Pageable pageable) {
        log.info("Start UserService - findAllUsers with pageable: {}", pageable);
        Page<User> usersPage = userRepository.findAll(pageable);
        log.info("End UserService - findAllUsers. Found {} users on page {}", usersPage.getTotalElements(), pageable.getPageNumber());
        return usersPage;
    }

    public List<User> findAllUsers() {
        log.info("Start UserService - findAllUsers");
        List<User> users = userRepository.findAll();
        log.info("End UserService - findAllUsers. Found {} users", users.size());
        return users;
    }

    public Optional<User> findUserById(Long userId) {
        log.info("Start UserService - findUserById with userId: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("End UserService - findUserById. Found user with id: {}", userId);
        } else {
            log.warn("End UserService - findUserById. No user found with id: {}", userId);
        }
        return user;
    }

    public void saveUser(User user) {
        log.info("Start UserService - saveUser with user: {}", user);
        userRepository.save(user);
        log.info("End UserService - saveUser. User saved: {}", user);
    }

    public void delete(User user) {
        log.info("Start UserService - delete with user: {}", user);
        userRepository.delete(user);
        log.info("End UserService - delete. User deleted: {}", user);
    }

    public List<User> findUsersByRole(String role) {
        log.info("Start UserService - findUsersByRole with role: {}", role);
        List<User> users = userRepository.findAllByRole(role);
        log.info("End UserService - findUsersByRole. Found {} users with role: {}", users.size(), role);
        return users;
    }

    public long countByGender(String gender) {
        log.info("Start UserService - countByGender with gender: {}", gender);
        long count = userRepository.countByGender(gender);
        log.info("End UserService - countByGender. Count of users with gender {}: {}", gender, count);
        return count;
    }

    public Page<User> searchUsers(String search, Pageable pageable) {
        log.info("Start UserService - searchUsers with search: {} and pageable: {}", search, pageable);
        Page<User> usersPage = userRepository.findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(search, search, search, search, search, pageable);
        log.info("End UserService - searchUsers. Found {} users matching '{}'", usersPage.getTotalElements(), search);
        return usersPage;
    }
}