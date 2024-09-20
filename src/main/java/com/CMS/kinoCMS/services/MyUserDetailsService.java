package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.models.User;
import com.CMS.kinoCMS.config.MyUserDetails;
import com.CMS.kinoCMS.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Start MyUserDetailsService - loadUserByUsername for username: {}", username);

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            log.info("End MyUserDetailsService - loadUserByUsername. User found: {}", username);
            return new MyUserDetails(user.get());
        } else {
            log.warn("End MyUserDetailsService - loadUserByUsername. User with username {} not found", username);
            throw new UsernameNotFoundException(username + " not found");
        }
    }
}
