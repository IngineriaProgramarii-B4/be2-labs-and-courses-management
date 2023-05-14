package com.example.signin.service;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import com.example.signin.exception.StudentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class TeacherService {
    private final TeachersRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;


    public TeacherService(TeachersRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addTeacher(Teacher teacherAuth) {
        System.out.println(teacherAuth);
        teacherRepository.save(teacherAuth);
    }

    @Transactional
    public void updateTeacher(String registrationNumber, String newPassword) {
        Teacher teacherAuthToUpdate = getTeacherByRegistrationNumber(registrationNumber);
        if (teacherAuthToUpdate != null) {
            teacherAuthToUpdate.setPassword(passwordEncoder.encode(newPassword));
            teacherRepository.save(teacherAuthToUpdate);
        }
        throw new StudentNotFoundException("Student_auth not found with registration number: " + registrationNumber);
    }

    @Transactional
    public Teacher getTeacherByRegistrationNumber(String registrationNumber) {
        return teacherRepository.findByRegistrationNumber(registrationNumber);
    }
}
