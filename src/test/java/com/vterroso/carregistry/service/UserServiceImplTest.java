package com.vterroso.carregistry.service;

import com.vterroso.carregistry.repository.UserRepository;
import com.vterroso.carregistry.repository.entity.UserEntity;
import com.vterroso.carregistry.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test");
        userEntity.setSurname("User");
        userEntity.setEmail("testuser@example.com");
        userEntity.setPassword("password");
        userEntity.setRole("ROLE_USER");
    }

    @Test
    void loadUserByUsername_UserFound() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(java.util.Optional.of(userEntity));

        UserDetailsService userDetailsService = userService.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@example.com");

        assertEquals("testuser@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByEmail("testuser@example.com");
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("unknownuser@example.com")).thenReturn(java.util.Optional.empty());

        UserDetailsService userDetailsService = userService.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknownuser@example.com"));

        verify(userRepository, times(1)).findByEmail("unknownuser@example.com");
    }

    @Test
    void saveUser() {
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity savedUser = userService.save(userEntity);
        assertEquals(userEntity, savedUser);

        verify(userRepository, times(1)).save(userEntity);
    }
}
