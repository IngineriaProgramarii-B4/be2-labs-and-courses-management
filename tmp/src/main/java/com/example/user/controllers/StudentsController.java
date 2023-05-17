package com.example.user.controllers;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.services.StudentsService;
import com.example.subject.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/")
public class StudentsController {
    private final StudentsService studentsService;
    private final SubjectService subjectService;
    @Autowired
    public StudentsController(StudentsService studentsService, SubjectService subjectService) {
        this.studentsService = studentsService;
        this.subjectService = subjectService;
    }

    @Operation(summary = "Get a list of students based on 0 or more filters passed as queries. The format is property_from_student_schema=value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found students that match the requirements",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Student.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found students that match the requirements",
                    content = @Content
            )
    })
    @GetMapping(value = {"/students"})
    public ResponseEntity<List<Student>> getStudentsByParams(@RequestParam Map<String, Object> params) {
        List<Student> students = studentsService.getStudentsByParams(params);

        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Operation(summary = "Receive necessary data in order to update information about a student in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found students that match the requirements",
                    content = @Content
            )
    })
    @PatchMapping(value = "/student/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable UUID id, @RequestBody Student student) {
        if (!studentsService.getStudentsByParams(Map.of("id", id)).isEmpty()) {
            studentsService.updateStudent(id, student);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Receive necessary data in order to add a new student in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource added successfully",
                    content = @Content)
    })
    @PostMapping(value = "/students")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        studentsService.saveStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @GetMapping("/students/enrolledCourse/{course}")
    public ResponseEntity<Set<Student>> getStudentsByCourse(@PathVariable String course) {
        Set<Student> students = studentsService.getStudentByEnrolledCourse(course);
        if(students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") String id) {
        Student student = studentsService.getStudentById(UUID.fromString(id));
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // <-------------------------------- FROM CATALOG ----------------------------------> //

    // La acest Get, cred ca e mai ok sa intoarcem doar ResponseEntity ca sa nu apara exception pe front
    @GetMapping("students/{id}/grades")
    public ResponseEntity<List<Grade>> getStudentByIdGrades(@PathVariable("id") String id) {
        Optional<Student> students = Optional.ofNullable(studentsService.getStudentById(UUID.fromString(id)));
        if (students.isPresent()) {
            return new ResponseEntity<>(students.get().getGrades(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ** //
    @GetMapping("students/{id}/{subject}")
    public List<Grade> getStudentByIdSubjectGrades(@PathVariable("id") UUID id, @PathVariable String subject) {
        Optional<Student> student = Optional.ofNullable(studentsService.getStudentById(id));
        if (student.isPresent()) {
            List<Grade> gradesList = student.get().getGradesBySubject(subject);
            if (gradesList.isEmpty()) {
                return List.of();
            }
            return new ResponseEntity<>(gradesList, HttpStatus.OK).getBody();
        }
        else return List.of();
    }

    @Nullable
    @DeleteMapping(value = "students/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> delete(@PathVariable("id") UUID id) {
        Optional<Student> isRemoved = Optional.ofNullable(studentsService.getStudentById(id));
        if (isRemoved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        isRemoved.get().setIsDeleted(true);
        return new ResponseEntity<>(isRemoved.get(), HttpStatus.OK);
    }

    //
    @GetMapping("students/{id}/grades/{gradeId}")
    @Nullable
    public ResponseEntity<Grade> getGradeById(@PathVariable("id") UUID id, @PathVariable("gradeId") UUID gradeId) {
        Optional<Student> student = Optional.ofNullable(studentsService.getStudentById(id));
        if (student.isPresent()) {
            Optional<Grade> grade = Optional.ofNullable(studentsService.getGradeById(id, gradeId));
            if (grade.isPresent()){
                return new ResponseEntity<>(grade.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //

    @PostMapping(path = "students/{id}/grades",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Nullable
    public ResponseEntity<Grade> addGrade(@PathVariable UUID id, @RequestBody Grade grade) {
        Optional <Student> students = Optional.ofNullable(studentsService.getStudentById(id));
        if (students.isPresent()
                && subjectService.getSubjectByTitle(grade.getSubject()).isPresent()
        ) {
            studentsService.addGrade(id, grade);
            return new ResponseEntity<>(grade, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ResponseEntity<> poate avea si un singur argument de HttpStatus, nu e necesar null
    @Nullable
    @DeleteMapping(value = "students/{id}/grades/{gradeId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Grade> deleteGrade(@PathVariable UUID id, @PathVariable UUID gradeId) {
        Grade isRemoved = studentsService.deleteGrade(id, gradeId);
        if (isRemoved == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isRemoved, HttpStatus.OK);
    }

    @Nullable
    @PutMapping("students/{id}/grades/{gradeId}")
    public ResponseEntity<Grade> updateGradeValue(@PathVariable("id") UUID id, @PathVariable("gradeId") UUID gradeId,@RequestParam(required = false) String evaluationDate,@RequestParam(required = false) Integer value){
        Optional<Student> student = Optional.ofNullable(studentsService.getStudentById(id));
        if (student.isPresent()) {
            Optional<Grade> grade = Optional.ofNullable(studentsService.getGradeById(id, gradeId));
            if (grade.isPresent()){
                studentsService.updateGrade(id,value,evaluationDate,gradeId);
                return new ResponseEntity<>(grade.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
