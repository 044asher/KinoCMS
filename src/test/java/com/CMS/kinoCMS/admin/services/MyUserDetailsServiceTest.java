package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    public void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setUsername("User");

        when(userRepository.findByUsername("User")).thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("User");

        assertNotNull(userDetails);
        assertEquals("User", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("User");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("NonExistentUser")).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () ->
                myUserDetailsService.loadUserByUsername("NonExistentUser")
        );

        assertEquals("NonExistentUser not found", thrown.getMessage());
        verify(userRepository, times(1)).findByUsername("NonExistentUser");
    }
}

