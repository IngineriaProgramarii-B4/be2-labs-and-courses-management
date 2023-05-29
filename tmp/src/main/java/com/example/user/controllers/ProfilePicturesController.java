package com.example.user.controllers;

import com.example.user.models.TupleProfilePicture;
import com.example.user.services.ProfilePicturesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1")
public class ProfilePicturesController {

    private final ProfilePicturesService profilePicturesService;

    public ProfilePicturesController(ProfilePicturesService profilePicturesService) {
        this.profilePicturesService = profilePicturesService;
    }

    @PostMapping("/profile/upload/{id}")
    public ResponseEntity<byte[]> uploadProfilePicture(@PathVariable("id") UUID idUser, @RequestParam("file") MultipartFile multipartFile) {
        if (profilePicturesService.uploadProfilePicture(multipartFile, idUser)) {
            return ResponseEntity.status(CREATED).body("File added successfully".getBytes());
        }
        return ResponseEntity.status(OK).body("File not added".getBytes());
    }

    @GetMapping("/profile/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable("id") UUID idUser) throws IOException {
        TupleProfilePicture file = profilePicturesService.download(idUser);
        if (file != null) {
            return ResponseEntity.ok().body(file.getBytes());
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }

    @DeleteMapping("/profile/delete/{filename}")
    public ResponseEntity<byte[]> delete(@PathVariable("filename") String filename) throws IOException {
        if (profilePicturesService.delete(filename)) {
            return ResponseEntity.status(NO_CONTENT).body("File deleted successfully".getBytes());
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }
}
