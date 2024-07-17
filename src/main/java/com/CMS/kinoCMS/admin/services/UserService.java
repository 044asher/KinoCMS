package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.repositories.UserRepository;
import com.CMS.kinoCMS.admin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findUsersByRole(String role){
        return userRepository.findAllByRole(role);
    }

    public long countByGender(String gender) {
        return userRepository.countByGender(gender);
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            throw new AccessDeniedException("Access Denied");
        }
    }

    public User updateUser(User user, String currentUsername) {
        User existingUser = findUserById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + user.getId()));

        if (!existingUser.getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Access Denied");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setDateOfBirthday(user.getDateOfBirthday());
        existingUser.setGender(user.getGender());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setCity(user.getCity());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        saveUser(existingUser);
        return existingUser;
    }

    public boolean isAuthorizedUser(long userId, String currentUsername) {
        User user = findUserById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        return user.getUsername().equals(currentUsername);
    }
}