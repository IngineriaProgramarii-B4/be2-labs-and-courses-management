package com.example.security.services;

import com.example.security.objects.Teacher;
import com.example.security.repositories.TeachersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TeachersService {
    private final TeachersRepository teachersRepository;

    public TeachersService(TeachersRepository teachersRepository) {
        this.teachersRepository = teachersRepository;
    }

    public List<Teacher> getTeachersByParams(Map<String, Object> params) {
        UUID id = null;
        String firstname = (String) params.get("firstname");
        String lastname = (String) params.get("lastname");
        String email = (String) params.get("email");
        String username = (String) params.get("username");
        String office = (String) params.get("office");
        String title = (String) params.get("title");

        if (params.containsKey("id") && (!params.get("id").equals(""))) {
            if (params.get("id").getClass().toString().equals("class java.lang.String")) {
                id = UUID.fromString((String) params.get("id"));
            } else {
                id = (UUID) params.get("id");
            }
        }

        return teachersRepository.findTeachersByParams(id, firstname, lastname, email, username, office, title);
    }

    public void saveTeacher(Teacher teacher) {
        teachersRepository.save(teacher);
    }

    @Transactional
    public void updateTeacher(UUID id, Teacher teacher) {
        // TODO : update the courses, it implies another table that makes connection between teacher and subjects
        teachersRepository.updateTeacher(id, teacher.getFirstname(), teacher.getLastname(), teacher.getEmail(), teacher.getUsername(), teacher.getOffice(), teacher.getTitle());
    }


//    public void updateTeachers() {
//        this.teachers = teacherRepository.findAll();
//    }
//
//    @Transactional
//    public void addTeacher(String firstName, String lastName, String degree, String mail, String password) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        Date createdAt = new Date(formatter.format(date));
//        Date updatedAt = new Date(formatter.format(date));
//        Teacher teacher = new Teacher(firstName, lastName, mail, password, degree, createdAt, updatedAt);
//        teacher.hashPassword();
//        teachers.add(teacher);
//        teacherRepository.save(teacher);
//    }
//
//    @Transactional
//    public void deleteTeacher(Teacher teacher1) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        teacherRepository.findById(teacher1.getId()).ifPresent(a1 -> {
//            a1.setDeleted(true);
//            a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            teacherRepository.save(a1);
//        });
//    }
//
//    @Transactional
//    public void updateTeacher(Teacher teacher1, String newFirstName, String newLastName) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        teacherRepository.findById(teacher1.getId()).ifPresent(a1 -> {
//            a1.setFirstName(newFirstName);
//            a1.setLastName(newLastName);
//            a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            teacherRepository.save(a1);
//        });
//    }
//
//    public List<Teacher> getTeacherByLastName(String lastName) {
//        return teacherRepository.findByLastName(lastName);
//    }
//
//    @Transactional
//    public List<Teacher> getTeacherByDegree(String degree) {
//        return teacherRepository.findByDegree(degree);
//    }
//
//    @Transactional
//    public Teacher getTeacherByMail(String mail) {
//        return teacherRepository.findByMail(mail);
//    }
//
}
