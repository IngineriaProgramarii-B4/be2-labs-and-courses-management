package com.example.signin.service;

import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.signin.exception.StudentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentsRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentsRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void addStudent(Student studentAuth) {
        //if student already exists throw error
        studentRepository.save(studentAuth);
    }
    @Transactional
    public void updateStudent(String registrationNumber,String newPassword) {
        Student studentAuthToUpdate = getStudentByRegistrationNumber(registrationNumber);
        if(studentAuthToUpdate != null){
            studentAuthToUpdate.setPassword(passwordEncoder.encode(newPassword));
            studentRepository.save(studentAuthToUpdate);
        }
        throw new StudentNotFoundException("Student_auth not found with registration number: " + registrationNumber);
    }
    @Transactional
    public Student getStudentByRegistrationNumber(String registrationNumber){
        return studentRepository.findByRegistrationNumber(registrationNumber);
    }
}
