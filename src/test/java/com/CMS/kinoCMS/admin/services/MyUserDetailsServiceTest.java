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
    public void testLoadUserByUsername(){
        User user = new User();
        user.setUsername("User");

        when(userRepository.findByUsername("User")).thenReturn(Optional.of(user));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("User");

        assertNotNull(userDetails);
        assertEquals("User", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("User");
    }

}
