package com.example.user.controllers;

import com.example.catalog.models.Grade;
import com.example.security.objects.Student;
import com.example.security.services.StudentsService;
import com.example.signin.security.EmailService;
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
    private final EmailService emailService;
    @Autowired
    public StudentsController(StudentsService studentsService, SubjectService subjectService, EmailService emailService) {
        this.studentsService = studentsService;
        this.subjectService = subjectService;
        this.emailService = emailService;
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
            @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found students that match the requirements",
                    content = @Content
            )
    })
    @PatchMapping(value = "/student/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable UUID id, @RequestBody Student student) {
        if (!studentsService.getStudentsByParams(Map.of("id", id)).isEmpty()) {
            studentsService.updateStudent(id, student);
            return new ResponseEntity<>(student, HttpStatus.NO_CONTENT);
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
    @Operation(summary = "Get a student with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found student that match the given id",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found students that match the given id",
                    content = @Content
            )
    })
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

    @Operation(summary = "Get the list of grades of the student with id={id}. The format is property_from_grade_schema=value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the grades of the student with the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Grade.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found the student with the given id.",
                    content = @Content
            )
    })
    @GetMapping("students/{id}/grades")
    public ResponseEntity<List<Grade>> getStudentByIdGrades(@PathVariable("id") String id) {
        Optional<Student> students = Optional.ofNullable(studentsService.getStudentById(UUID.fromString(id)));
        if (students.isPresent()) {
            return new ResponseEntity<>(students.get().getGrades(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get the list of grades of the student with id={id} on a certain subject. The format is property_from_grade_schema=value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the grades on the given subject of the student with the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Grade.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found the student with the given id.",
                    content = @Content
            )
    })
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
    @Operation(summary = "Get the grade with the id={gradeId} of the student with id={id}. The format is property_from_grade_schema=value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the grade with the given id of the student with the given id.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Grade.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found the student or the grade with the given id.",
                    content = @Content
            )
    })
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
    @Operation(summary = "Soft deleting an existing student from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Haven't found the student with the given id.",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Student soft deleted successfully",
                    content = @Content)
    })
    @Nullable
    @DeleteMapping(value = "students/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") UUID id) {
        Optional<Student> isRemoved = Optional.ofNullable(studentsService.getStudentById(id));
        if (isRemoved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        isRemoved.get().setIsDeleted(true);
        return new ResponseEntity<>(isRemoved.get(), HttpStatus.OK);
    }

    @Operation(summary = "Receive necessary data in order to add a new grade in the student's grades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource added successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found the student with the given id.",
                    content = @Content)
    })
    @PostMapping(path = "students/{id}/grades",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Nullable
    public ResponseEntity<Grade> addGrade(@PathVariable UUID id, @RequestBody Grade grade) {
        Optional <Student> student = Optional.ofNullable(studentsService.getStudentById(id));
        if (student.isPresent()
                && subjectService.getSubjectByTitle(grade.getSubject()).isPresent()) {
            studentsService.addGrade(id, grade);
            emailService.sendGradeAddedEmail(student.get().getEmail(), grade);
            return new ResponseEntity<>(grade, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Soft deleting an existing grade from the student's grades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Haven't found the student or the grade with the given id.",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Grade soft deleted successfully",
                    content = @Content)
    })
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
    @Operation(summary = "Receive necessary data in order to update information about a student's certain grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found student or grade with given id",
                    content = @Content
            )
    })
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
