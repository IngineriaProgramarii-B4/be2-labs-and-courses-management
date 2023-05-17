package com.example.signin.service;

import com.example.security.objects.Admin;
import com.example.security.objects.Teacher;
import com.example.security.repositories.AdminsRepository;
import com.example.signin.exception.StudentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminsRepository adminsRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminsRepository adminsRepository, PasswordEncoder passwordEncoder) {
        this.adminsRepository = adminsRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void addAdmin(Admin adminAuth) {
        adminsRepository.save(adminAuth);
    }

    @Transactional
    public void updateAdmin(String registrationNumber, String newPassword) {
        Admin adminAuthToUpdate = getAdminByRegistrationNumber(registrationNumber);
        if (adminAuthToUpdate != null) {
            adminAuthToUpdate.setPassword(passwordEncoder.encode(newPassword));
            adminsRepository.save(adminAuthToUpdate);
        } else {
            throw new StudentNotFoundException("Student_auth not found with registration number: " + registrationNumber);
        }
    }

    @Transactional
    public Admin getAdminByRegistrationNumber(String registrationNumber) {
        return adminsRepository.findByRegistrationNumber(registrationNumber);
    }

}
