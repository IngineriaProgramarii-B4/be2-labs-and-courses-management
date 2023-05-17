/*
package com.example.demo;

import com.example.demo.objects.Student;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    public void testAddStudent() {
        String registrationNumber = "A112";
        String firstName = "John";
        String lastName = "Doe";
        int year = 2;
        String grupa = "A2";
        String mail = "johndoe@example.com";
        String password = "password";

        studentService.addStudent(registrationNumber, firstName, lastName, year, grupa, mail, password);
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void testGetStudentByRegistrationNumber() {
        // Given
        String registrationNumber = "A112";
        String firstName = "John";
        String lastName = "Doe";
        int year = 2;
        String grupa = "A2";
        String mail = "johndoe@example.com";
        String password = "password";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));

        Student expectedStudent = new Student(registrationNumber, firstName, lastName, year, grupa, mail, password, createdAt, updatedAt);

        when(studentRepository.findByRegistrationNumber(registrationNumber)).thenReturn(expectedStudent);

        // When
        Student retrievedStudent = studentService.getStudentByRegistrationNumber(registrationNumber);

        // Then
        Assertions.assertNotNull(retrievedStudent);
        assertEquals(expectedStudent.getFirstName(), retrievedStudent.getFirstName());
        assertEquals(expectedStudent.getLastName(), retrievedStudent.getLastName());
        assertEquals(expectedStudent.getRegistrationNumber(), retrievedStudent.getRegistrationNumber());

        String newRegistrationNumber ="309RO1232";
        expectedStudent.setRegistrationNumber(newRegistrationNumber);
        assertEquals(newRegistrationNumber, expectedStudent.getRegistrationNumber());
    }

    @Test
    public void testGetStudentByYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        Student s1 = new Student("A112", "John", "Doe", 1, "A2", "johndoe@example.com", "password", createdAt, updatedAt);
        Student s2 = new Student("B225", "Jane", "Doe", 2, "B4", "janedoe@example.com", "mama", createdAt, updatedAt);
        when(studentRepository.findByYear(1)).thenReturn(Arrays.asList(s1));
        when(studentRepository.findByYear(2)).thenReturn(Arrays.asList(s2));

        List<Student> students1 = studentService.getStudentByYear(1);
        List<Student> students2 = studentService.getStudentByYear(2);

        assertEquals(1, students1.size());
        assertEquals(s1.getRegistrationNumber(), students1.get(0).getRegistrationNumber());
        assertEquals(1, students2.size());
        assertEquals(s2.getRegistrationNumber(), students2.get(0).getRegistrationNumber());
    }

    @Test
    public void testGetStudentByGrupaAndYear() {
        List<Student> expectedStudents = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        expectedStudents.add(new Student("123456", "John", "Doe", 2, "A", "johndoe@example.com", "password", createdAt, updatedAt));
        expectedStudents.add(new Student("654321", "Jane", "Doe", 2, "A", "janedoe@example.com", "mama", createdAt, updatedAt));
        when(studentRepository.findByGrupaAndYear(anyString(), anyInt())).thenReturn(expectedStudents);

        List<Student> actualStudents = studentService.getStudentByGrupaAndYear("A", 2022);

        assertEquals(expectedStudents.size(), actualStudents.size());
        assertEquals(expectedStudents.get(0).getFirstName(), actualStudents.get(0).getFirstName());
        assertEquals(expectedStudents.get(0).getLastName(), actualStudents.get(0).getLastName());
        assertEquals(expectedStudents.get(1).getFirstName(), actualStudents.get(1).getFirstName());
        assertEquals(expectedStudents.get(1).getLastName(), actualStudents.get(1).getLastName());
    }

    @Test
    void testGetStudentByFirstName() {
        List<Student> students = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        Student student1 = new Student("111111", "John", "Doe", 1, "A", "johndoe@example.com", "password", createdAt, updatedAt);
        Student student2 = new Student("222222", "John", "Smith", 2, "B", "johnsmith@example.com", "password", createdAt, updatedAt);
        students.add(student1);
        students.add(student2);

        when(studentRepository.findByFirstName("John")).thenReturn(students);

        List<Student> result = studentService.getStudentByFirstName("John");
        assertEquals(2, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("Smith", result.get(1).getLastName());
    }

    @Test
    void testGetStudentByLastName() {
        List<Student> students = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Date createdAt= new Date(formatter.format(date));
        Date updatedAt= new Date(formatter.format(date));
        Student student1 = new Student("111111", "John", "Doe", 1, "A", "johndoe@example.com", "password", createdAt, updatedAt);
        Student student2 = new Student("222222", "Marie", "Doe", 2, "B", "mariedoe@example.com", "password", createdAt, updatedAt);
        students.add(student1);
        students.add(student2);

        when(studentRepository.findByLastName("Doe")).thenReturn(students);

        List<Student> result = studentService.getStudentByLastName("Doe");
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Marie", result.get(1).getFirstName());
    }

    @Test
    public void testDeleteStudent() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Student student = new Student("111111", "John", "Doe", 1, "A", "johndoe@example.com", "password", createdAt, updatedAt);

        doReturn(Optional.of(student)).when(studentRepository).findById(null);

        studentService.deleteStudent(student);

        verify(studentRepository, times(1)).save(any(Student.class));
        assertEquals(true, student.getIsDeleted());
    }

    @Test
    public void testUpdateStudent() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date createdAt= new java.util.Date(formatter.format(date));
        java.util.Date updatedAt= new java.util.Date(formatter.format(date));
        Student student = new Student("111111", "John", "Doe", 1, "A", "johndoe@example.com", "password", createdAt, updatedAt);


        doReturn(Optional.of(student)).when(studentRepository).findById(null);


        String newName = "Jane";
        String newSurname = "Smith";
        studentService.updateStudent(student,newName, newSurname);

        verify(studentRepository, times(1)).save(any(Student.class));

        assertEquals(newName, student.getFirstName());
        assertEquals(newSurname, student.getLastName());

        // Alte verificari pentru set-uri:
        UUID verificareSetId = UUID.randomUUID();
        Integer anVerificare = 3;
        String grupaVerificare ="B5";
        String parolaVerificare ="bbbb";
        String mailVerifiacre ="aaa@gmail.com";

        student.setYear(anVerificare);
        student.setId(verificareSetId);
        student.setGrupa(grupaVerificare);
        student.setPassword(parolaVerificare);
        student.setMail(mailVerifiacre);

        assertEquals(verificareSetId, student.getId());
        assertEquals(grupaVerificare, student.getGrupa());
        assertEquals(anVerificare, student.getYear());
        assertEquals(parolaVerificare,student.getPassword());
        assertEquals(mailVerifiacre, student.getMail());
    }
}
*/
