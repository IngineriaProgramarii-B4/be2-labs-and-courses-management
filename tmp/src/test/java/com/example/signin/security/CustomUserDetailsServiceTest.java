package com.example.signin.security;

import com.example.security.objects.User;
import com.example.security.repositories.UsersRepository;
import com.example.signin.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("USER");

        user = mock(User.class, Mockito.withSettings().lenient());
        when(user.getEmail()).thenReturn("test@example.com");
        when(user.getPassword()).thenReturn("password123");
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
    }
    @Test
    void loadUserByUsername_withValidUsername() {
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        verify(usersRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsername_withInvalidUsername() {
        when(usersRepository.findByEmail("invalid@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("invalid@example.com");
        });

        verify(usersRepository, times(1)).findByEmail("invalid@example.com");
    }
}