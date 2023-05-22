
package com.example.subject.api;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.subject.model.Subject;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final FirebaseStorageStrategy firebaseStorageStrategy;

    @Autowired
    public SubjectController(SubjectService subjectService, FirebaseStorageStrategy firebaseStorageStrategy) {
        this.subjectService = subjectService;
        this.firebaseStorageStrategy = firebaseStorageStrategy;
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> addSubject(@RequestBody Subject subject)
    {
        if(subjectService.addSubject(subject) == 0)
            return ResponseEntity.status(NOT_ACCEPTABLE).body("Subject already exists or is invalid".getBytes());
        return ResponseEntity.status(CREATED).body("Subject added successfully".getBytes());
    }

    @DeleteMapping("subjectTitle={title}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> deleteSubjectByTitle(@PathVariable("title") String title) {
        if(subjectService.deleteSubjectByTitle(title) == 0)
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        return ResponseEntity.status(NO_CONTENT).body("Subject deleted successfully".getBytes());
    }

    @PutMapping("subjectTitle={title}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> updateSubjectByTitle(@PathVariable("title") String title, @RequestBody Subject subject) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(subjectService.updateSubjectByTitle(title, subject) == 0)
            return ResponseEntity.status(NOT_ACCEPTABLE).body("Subject is invalid".getBytes());
        return ResponseEntity.status(NO_CONTENT).body("Subject updated successfully".getBytes());
    }

    @GetMapping(path = "subjectTitle={title}")
    public ResponseEntity<Subject> getSubjectByTitle(@PathVariable("title") String title) {
        return subjectService.getSubjectByTitle(title).map(subject -> ResponseEntity.status(OK).body(subject))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @GetMapping(path = "year={year}&semester={semester}")
    public List<Subject> getSubjectsByYearAndSemester(@PathVariable("year") int year, @PathVariable("semester") int semester) {
        return subjectService.getSubjectsByYearAndSemester(year, semester);
    }

    @PutMapping(path = "subjectTitle={title}/image")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> uploadSubjectImage(@PathVariable("title") String title, @RequestParam("image")MultipartFile image) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(subjectService.uploadSubjectImage(title, image) == 0)
            return ResponseEntity.status(NOT_ACCEPTABLE).body("Image is invalid".getBytes());
        return ResponseEntity.status(NO_CONTENT).body("Image uploaded successfully".getBytes());
    }
    @GetMapping(path = "subjectTitle={title}/image")
    public ResponseEntity<byte[]> getSubjectImage(@PathVariable("title") String title) {
        Optional<Subject> subjectMaybe = subjectService.getSubjectByTitle(title);
        if(subjectMaybe.isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        try {
            /*byte[] img = Files.readAllBytes(new File(subjectMaybe.get().getImage().getLocation()).toPath());*/
            byte[] img = firebaseStorageStrategy.download(subjectMaybe.get().getImage().getLocation());
            return ResponseEntity
                    .status(OK)
                    .contentType(MediaType.valueOf(subjectMaybe.get().getImage().getType()))
                    .body(img);
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body("Image not found".getBytes());
        }
    }
}
