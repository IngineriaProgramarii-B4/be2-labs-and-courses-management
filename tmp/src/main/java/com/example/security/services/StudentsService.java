package com.example.security.services;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.repositories.StudentsRepository;
import com.example.subject.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Service
public class StudentsService {
    private final StudentsRepository studentsRepository;

    public StudentsService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public List<Student> getStudentsByParams(Map<String, Object> params) {
        UUID id = null;
        String firstname = (String) params.get("firstname");
        String lastname = (String) params.get("lastname");
        String email = (String) params.get("email");
        String username = (String) params.get("username");
        int year = 0;
        int semester = 0;
        String registrationNumber = (String) params.get("registrationNumber");

        final String idKey = "id";
        if (params.containsKey(idKey) && (!params.get(idKey).equals(""))) {
            id = (UUID) params.get(idKey);

        }
        final String yearKey = "year";
        if (params.containsKey(yearKey) && (!params.get(yearKey).equals(""))) {
            year = Integer.parseInt((String) params.get(yearKey));

        }
        final String semesterKey = "semester";
        if (params.containsKey(semesterKey) && (!params.get(semesterKey).equals(""))) {
            semester = Integer.parseInt((String) params.get(semesterKey));
        }
        return studentsRepository.findStudentsByParams(id, firstname, lastname, email, username, year, semester, registrationNumber);
    }

    public Student saveStudent(Student student) {
        if (student == null)
            return null;
        studentsRepository.save(student);
        return student;
    }

    @Transactional
    public Student deleteStudent(UUID id) {
        Student student = studentsRepository.findStudentById(id);
        if (student == null)
            return null;
        /* Stergem notele studentului: */
        List<Grade> grades = student.getGrades();
        for(Grade grade : grades) {
            grade.setIsDeleted(true);
        }

        return (Student) student.setIsDeleted(true);
    }

    @Transactional
    public void updateStudent(UUID id, Student student) {
        // TODO : update the courses, it implies another table that makes connection between teacher and subjects
        studentsRepository.updateStudent(id, student.getFirstname(), student.getLastname(), student.getEmail(), student.getUsername(), student.getYear(), student.getSemester(), student.getRegistrationNumber());
    }

    public Set<Student> getStudentByEnrolledCourse(String course) {
        List<Student> allStudents = studentsRepository.findStudentsByParams(null, null, null, null, null, 0, 0, null);
        Set<Student> searchedStudents = new HashSet<>();
        for(Student st : allStudents) {
            for(Subject c: st.getEnrolledCourses()) {
                if(c.getTitle().equals(course)) {
                    searchedStudents.add(st);
                }
            }
        }
        return searchedStudents;
    }


//    public void updateStudents(){x
//        students=studentRepository.findAll();
//    }
//    @Transactional
//    public void addStudent(String registrationNumber, String firstName, String lastName,int year, String grupa, String mail, String password) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        Date createdAt= new Date(formatter.format(date));
//        Date updatedAt= new Date(formatter.format(date));
//        Student student = new Student(registrationNumber,firstName,lastName,year,grupa,mail,password,createdAt,updatedAt);
//
//        student.hashPassword();
//        students.add(student);
//        studentRepository.save(student);
//    }
//    @Transactional
//    public void deleteStudent(Student student) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        studentRepository.findById(student.getId()).ifPresent(a1->{ a1.setDeleted(true);a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            studentRepository.save(a1);});
//    }
//
//    @Transactional
//    public void updateStudent(Student student, String newFirstName, String newLastName) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        studentRepository.findById(student.getId()).ifPresent(a1->{ a1.setFirstName(newFirstName);a1.setLastName(newLastName);a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            studentRepository.save(a1);});
//    }
//    @Transactional
//    public Student getStudentByRegistrationNumber(String registrationNumber){
//        return studentRepository.findByRegistrationNumber(registrationNumber);
//    }
//
//    @Transactional
//    public List<Student> getStudentByYear(int year){
//        return studentRepository.findByYear(year);
//    }
//
//    @Transactional
//    public List<Student> getStudentByGrupaAndYear(String grupa, int year) {
//        return studentRepository.findByGrupaAndYear(grupa, year);
//    }
//    @Transactional
//    public List<Student> getStudentByFirstName(String firstName){
//        return studentRepository.findByFirstName(firstName);
//    }
//    @Transactional
//    public List<Student> getStudentByLastName(String lastName){
//        return studentRepository.findByLastName(lastName);
//    }
//

    // <-------------------------------- FROM CATALOG ----------------------------------> //

    // Q: te poti folosi de getStudentsByParams - A: nu, ca da o lista de studenti si n-am nevoie de o lista daca sigur o sa-mi returneze un singur student
    public Student getStudentById(UUID id) {
        return studentsRepository.findStudentById(id);
    }

    // Q: asta nu ar trebui in GradeService? ca si toate cele de mai jos, avand in vedere ca returneaza Grade - A: idk, apeleaza functii din studentService, idk what to say
    public Grade getGradeById(UUID id, UUID gradeId) {
        Student student = studentsRepository.findStudentById(id);
        Grade grade = student.getGradeById(gradeId);
        if (grade != null) {
            return grade;
        } else {
            throw new IllegalStateException("There is no grade with the id " + gradeId);
        }
    }

    @Transactional
    public Grade addGrade(UUID id, Grade grade) {
        Student student = getStudentById(id);
        student.addGrade(grade);
        return grade;
    }

    @Transactional
    public Grade deleteGrade(UUID id, UUID gradeId) {
        try {
            return getGradeById(id, gradeId).setDeleted();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Grade updateGrade(UUID id, Integer value, String evaluationDate, UUID gradeId) {
        Student student = studentsRepository.findStudentById(id);

        if (student == null) {
            return null;
        }

        Grade grade = getGradeById(id, gradeId);

        if (value != null && (value < 1 || value > 10)) {
            throw new IllegalStateException("The value is not between 1 and 10");
        }

        if (value != null && value != grade.getValue()) {
            grade.setValue(value);
        }

        if (evaluationDate != null && !evaluationDate.equals(grade.getEvaluationDate())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                LocalDate.parse(evaluationDate, formatter);
                grade.setEvaluationDate(evaluationDate);
            } catch (DateTimeParseException exception) {
                throw new IllegalStateException("The date doesn't match the required format. "+exception);
            }
        }

        return grade;
    }

}
