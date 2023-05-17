package com.example.demo;


import com.example.demo.objects.Admin;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminService adminService;
    @BeforeEach
    public void setUp() {
        adminService = new AdminService(adminRepository);
    }

    @Test
    public void testAddAdmins() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "John";
        String surname = "Doe";

        // When
        adminService.addAdmins(name, surname);

        // Then
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteAdmins() {
        String name = "John";
        String surname = "Doe";


        Admin admin = new Admin(name, surname, null, null);
        doReturn(Optional.of(admin)).when(adminRepository).findById(null);

        adminService.deleteAdmins(admin);

        verify(adminRepository, times(1)).save(any(Admin.class));
        assertEquals(true, admin.getIsDeleted());
    }

    @Test
    public void testUpdateAdmins() {
        String name = "John";
        String surname = "Doe";

        Admin admin = new Admin(name, surname, null, null);

        doReturn(Optional.of(admin)).when(adminRepository).findById(null);


        String newName = "Jane";
        String newSurname = "Smith";
        adminService.updateAdmins(admin,newName, newSurname);

        verify(adminRepository, times(1)).save(any(Admin.class));

        assertEquals(newName, admin.getName());
        assertEquals(newSurname, admin.getSurname());

        UUID verificareSetId = UUID.randomUUID();

        admin.setId(verificareSetId);

        assertEquals(verificareSetId, admin.getId());
    }

    @Test
    public void testGetAdminByName() {
        // Create a list of admins for testing
        List<Admin> admins = new ArrayList<>();
        String name = "John";
        String surname = "Doe";
        Admin admin = new Admin(name, surname, null, null);
        admins.add(admin);

        // Mock the behavior of the repository
        when(adminRepository.findByName(name)).thenReturn(admins);

        // Create an instance of the service and call the method being tested
        AdminService adminService = new AdminService(adminRepository);
        List<Admin> result = adminService.getAdminByName(name);

        // Check that the result is correct
        assertEquals(admins, result);
    }

    @Test
    public void testGetAdminBySurname() {
        List<Admin> admins = new ArrayList<>();
        UUID id = UUID.randomUUID();
        String name = "John";
        String surname = "Doe";
        Admin admin = new Admin(name, surname, null, null);
        admins.add(admin);

        when(adminRepository.findBySurname(surname)).thenReturn(admins);

        AdminService adminService = new AdminService(adminRepository);
        List<Admin> result = adminService.getAdminBySurname(surname);

        assertEquals(admins, result);
    }

    @Test
    public void testGetAdminByNameAndSurname() {
        // Create a sample admin object
        List<Admin> admins = new ArrayList<>();
        UUID adminId = UUID.randomUUID();
        String adminName = "John";
        String adminSurname = "Doe";
        Admin sampleAdmin = new Admin(adminName, adminSurname, null, null);
        admins.add(sampleAdmin);

        // Set up the mock repository to return the sample admin object
        when(adminRepository.findByNameAndSurname(adminName, adminSurname)).thenReturn(admins);

        // Call the method being tested
        List<Admin> returnedAdmin = adminService.getAdminByNameAndSurname(adminName, adminSurname);

        // Verify the result
        assertEquals(admins, returnedAdmin);
    }


}