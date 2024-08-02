package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.models.User;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public void testFindAllUsersPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User(), new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.findAllUsers(pageable);
        assertEquals(userPage, result);
        verify(userRepository, times(1)).findAll(pageable);
    }

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

    @Test
    public void testSearchUsers() {
        String search = "test";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User(), new User());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(
                search, search, search, search, search, pageable)).thenReturn(userPage);

        Page<User> result = userService.searchUsers(search, pageable);
        assertEquals(userPage, result);
        verify(userRepository, times(1)).findByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(
                search, search, search, search, search, pageable);
    }
}
