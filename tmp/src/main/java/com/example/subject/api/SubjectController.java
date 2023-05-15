
package com.example.subject.api;

import com.example.subject.model.Subject;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/v1/subjects")
@CrossOrigin(origins = "http://localhost:3000")
public class SubjectController {
    
    private static final String SUBJECT_ERROR = "Subject not found";

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public void addSubject(@RequestBody Subject subject)
    {
        if(subjectService.addSubject(subject) == 0)
            throw new ResponseStatusException(NOT_ACCEPTABLE, "Subject already exists or is invalid");
        throw new ResponseStatusException(CREATED, "Subject added successfully");
    }

    @DeleteMapping("subjectTitle={title}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public void deleteSubjectByTitle(@PathVariable("title") String title) {
        if(subjectService.deleteSubjectByTitle(title) == 0)
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);
        throw new ResponseStatusException(NO_CONTENT, "Subject deleted successfully");
    }

    @PutMapping("subjectTitle={title}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public void updateSubjectByTitle(@PathVariable("title") String title, @RequestBody Subject subject) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);
        if(subjectService.updateSubjectByTitle(title, subject) == 0)
            throw new ResponseStatusException(NOT_ACCEPTABLE, "Subject is invalid");
        throw new ResponseStatusException(NO_CONTENT, "Subject updated successfully");
    }

    @GetMapping(path = "subjectTitle={title}")
    public Subject getSubjectByTitle(@PathVariable("title") String title) {
        return subjectService.getSubjectByTitle(title)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR));
    }

    @GetMapping(path = "year={year}&semester={semester}")
    public List<Subject> getSubjectsByYearAndSemester(@PathVariable("year") int year, @PathVariable("semester") int semester) {
        return subjectService.getSubjectsByYearAndSemester(year, semester);
    }

    @PutMapping(path = "subjectTitle={title}/image")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public void uploadSubjectImage(@PathVariable("title") String title, @RequestParam("image")MultipartFile image) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);
        if(subjectService.uploadSubjectImage(title, image) == 0)
            throw new ResponseStatusException(NOT_ACCEPTABLE, "Image is invalid");
        throw new ResponseStatusException(NO_CONTENT, "Image uploaded successfully");
    }
    @GetMapping(path = "subjectTitle={title}/image")
    public ResponseEntity<byte[]> getSubjectImage(@PathVariable("title") String title) {
        Optional<Subject> subjectMaybe = subjectService.getSubjectByTitle(title);
        if(subjectMaybe.isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        try {
            byte[] img = Files.readAllBytes(new File(subjectMaybe.get().getImage().getLocation()).toPath());
            return ResponseEntity
                    .status(OK)
                    .contentType(MediaType.valueOf(subjectMaybe.get().getImage().getType()))
                    .body(img);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body("Image not found".getBytes());
        }
    }
}
