package com.example.signin.service;

import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import com.example.signin.exception.StudentNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
    @Mock
    private AdminsRepository adminsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;

    private final String NEWPASSWORD="newPassword";
    private final String REGISTRATIONNUMBER="1";
    private final String NONEXISTINGREGISTRATIONNUMBER="1";
    private final String UNKNOWNREGNUMBER="1";

    @Before
    public void setUp() {
        admin = new Admin();
        admin.setRegistrationNumber("1");
        admin.setPassword("password");
    }

    @Test
    public void addAdmin() {
        // Arrange
        adminService.addAdmin(admin);
        // Assert
        verify(adminsRepository, times(1)).save(admin);
    }

    @Test(expected = StudentNotFoundException.class)
    public void updateAdminNotFound() {
        // Act
        when(adminsRepository.findByRegistrationNumber(UNKNOWNREGNUMBER)).thenReturn(null);

        // Assert
        adminService.updateAdmin(UNKNOWNREGNUMBER, NEWPASSWORD);
    }

    @Test
    public void getAdminByRegistrationNumber() {
        // Act
        when(adminsRepository.findByRegistrationNumber(admin.getRegistrationNumber())).thenReturn(admin);

        Admin foundAdmin = adminService.getAdminByRegistrationNumber(admin.getRegistrationNumber());

        // Assert
        assertEquals(admin, foundAdmin);
    }

    @Test
    public void testUpdateAdmin() {

        // Arrange
        Admin existingAdmin = new Admin();
        existingAdmin.setRegistrationNumber(REGISTRATIONNUMBER);

        // Act
        when(adminsRepository.findByRegistrationNumber(REGISTRATIONNUMBER)).thenReturn(existingAdmin);
        when(passwordEncoder.encode(NEWPASSWORD)).thenReturn("encodedPassword");

        adminService.updateAdmin(REGISTRATIONNUMBER, NEWPASSWORD);

        // Assert
        verify(adminsRepository, times(1)).save(existingAdmin);
        assertEquals("encodedPassword", existingAdmin.getPassword());
    }

    @Test(expected = StudentNotFoundException.class)
    public void testUpdateAdmin_ThrowsException() {

        // Act
        when(adminsRepository.findByRegistrationNumber(NONEXISTINGREGISTRATIONNUMBER)).thenReturn(null);

        // Assert
        adminService.updateAdmin(NONEXISTINGREGISTRATIONNUMBER, NEWPASSWORD);
    }
}
