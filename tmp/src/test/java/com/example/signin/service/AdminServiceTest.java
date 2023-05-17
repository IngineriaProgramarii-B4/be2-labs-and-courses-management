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

    @Before
    public void setUp() {
        admin = new Admin();
        admin.setRegistrationNumber("1");
        admin.setPassword("password");
    }

    @Test
    public void addAdmin() {
        adminService.addAdmin(admin);
        verify(adminsRepository, times(1)).save(admin);
    }

    @Test(expected = StudentNotFoundException.class)
    public void updateAdminNotFound() {
        String newPassword = "newPassword";
        String unknownRegNumber = "unknown";

        when(adminsRepository.findByRegistrationNumber(unknownRegNumber)).thenReturn(null);

        adminService.updateAdmin(unknownRegNumber, newPassword);
    }

    @Test
    public void getAdminByRegistrationNumber() {
        when(adminsRepository.findByRegistrationNumber(admin.getRegistrationNumber())).thenReturn(admin);

        Admin foundAdmin = adminService.getAdminByRegistrationNumber(admin.getRegistrationNumber());

        assertEquals(admin, foundAdmin);
    }

    @Test
    public void testUpdateAdmin() {
        String registrationNumber = "1"; // Update the registration number here
        String newPassword = "newPassword";

        Admin existingAdmin = new Admin();
        existingAdmin.setRegistrationNumber(registrationNumber);

        when(adminsRepository.findByRegistrationNumber(registrationNumber)).thenReturn(existingAdmin);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        adminService.updateAdmin(registrationNumber, newPassword);

        verify(adminsRepository, times(1)).save(existingAdmin);
        assertEquals("encodedPassword", existingAdmin.getPassword());
    }

    @Test(expected = StudentNotFoundException.class)
    public void testUpdateAdmin_ThrowsException() {
        String nonExistingRegistrationNumber = "1234";
        String newPassword = "newPassword";

        when(adminsRepository.findByRegistrationNumber(nonExistingRegistrationNumber)).thenReturn(null);

        adminService.updateAdmin(nonExistingRegistrationNumber, newPassword);
    }
}
