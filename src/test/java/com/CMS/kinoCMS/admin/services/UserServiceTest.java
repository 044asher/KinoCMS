package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindAllUsers() {
        List<User> users = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        userService.delete(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testFindUsersByRole() {
        String role = "ADMIN";
        List<User> users = List.of(new User(), new User());

        when(userRepository.findAllByRole(role)).thenReturn(users);

        List<User> result = userService.findUsersByRole(role);
        assertEquals(users, result);
        verify(userRepository, times(1)).findAllByRole(role);
    }

    @Test
    public void testCountByGender() {
        String gender = "Male";
        long count = 5L;

        when(userRepository.countByGender(gender)).thenReturn(count);

        long result = userService.countByGender(gender);
        assertEquals(count, result);
        verify(userRepository, times(1)).countByGender(gender);
    }
}

