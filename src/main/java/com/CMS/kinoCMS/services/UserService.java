package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

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

    public User saveUser(User user) {
        return userRepository.save(user);
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
}
