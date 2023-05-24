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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
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

    private final String EMAIL="test@example.com";

    private final String INVALID="invalid@example.com";

    private final String PASSWORD="password123";

    private final String USER="USER";


    @BeforeEach
    void setUp() {

        Role role = new Role();
        role.setName(USER);

        user = mock(User.class, Mockito.withSettings().lenient());
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(user.getRoles()).thenReturn(Collections.singletonList(role));
    }

    @Test
    void loadUserByUsername_withValidUsername() {

        // Act
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        verify(usersRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsername_withInvalidUsername() {

        // Arrange
        when(usersRepository.findByEmail(INVALID)).thenReturn(null);

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(INVALID);
        });

        verify(usersRepository, times(1)).findByEmail(INVALID);
    }
}