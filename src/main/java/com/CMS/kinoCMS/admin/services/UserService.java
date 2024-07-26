package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        log.info("Finding user by id: {}", userId);
        return userRepository.findById(userId);
    }

    public void saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        log.info("Saving user: {}", user);
        userRepository.save(user);
    }

    public void delete(User user) {
        log.info("Deleting user: {}", user);
        userRepository.delete(user);
    }

    public List<User> findUsersByRole(String role) {
        log.info("Finding users by role: {}", role);
        return userRepository.findAllByRole(role);
    }

    public long countByGender(String gender) {
        log.info("Counting users by gender: {}", gender);
        return userRepository.countByGender(gender);
    }
}
