package com.example.user.controllers;

import com.example.security.objects.Teacher;
import com.example.security.services.TeachersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/")
public class TeachersController {
    private final TeachersService teachersService;

    @Autowired
    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @Operation(summary = "Get a list of teachers based on 0 or more filters passed as queries. The format is property_from_teacher_schema=value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found teachers that match the requirements",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Teacher.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found teachers that match the requirements",
                    content = @Content
            )
    })
    @GetMapping(value = "/teachers")
    public ResponseEntity<List<Teacher>> getTeachersByParams(@RequestParam Map<String, Object> params) {
        List<Teacher> teachers = teachersService.getTeachersByParams(params);

        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @Operation(summary = "Receive necessary data in order to update information about a teacher in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found the teacher",
                    content = @Content
            )
    })
    @PatchMapping(value = "/teacher/{id}")
    public ResponseEntity<String> updateTeacher(@PathVariable UUID id, @RequestBody Teacher teacher) {
        if (!teachersService.getTeachersByParams(Map.of("id", id)).isEmpty()) {
            teachersService.updateTeacher(id, teacher);
            return new ResponseEntity<>("Resource updated successfully", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Haven't found the teacher", HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Receive necessary data in order to add a new teacher in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource added successfully",
                    content = @Content)
    })
    @PostMapping(value = "/teachers")
    public ResponseEntity<String> saveTeacher(@RequestBody Teacher teacher) {
        teachersService.saveTeacher(teacher);
        return new ResponseEntity<>("Resource added successfully", HttpStatus.CREATED);
    }
}
