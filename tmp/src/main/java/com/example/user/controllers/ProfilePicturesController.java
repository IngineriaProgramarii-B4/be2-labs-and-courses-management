package com.example.user.controllers;

import com.example.user.services.ProfilePicturesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1")
public class ProfilePicturesController {

    private final ProfilePicturesService profilePicturesService;

    public ProfilePicturesController(ProfilePicturesService profilePicturesService) {
        this.profilePicturesService = profilePicturesService;
    }

    @PostMapping("/profile/upload")
    public ResponseEntity<byte[]>  uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile) {
        if (profilePicturesService.uploadProfilePicture(multipartFile, multipartFile.getOriginalFilename())) {
            return ResponseEntity.status(CREATED).body("File added successfully".getBytes());
        }
        return ResponseEntity.status(OK).body("File not added".getBytes());
    }

    @GetMapping("/profile/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String path) throws IOException {
        byte[] file = profilePicturesService.download(path);
        if (file != null) {
            return ResponseEntity.ok().body(file);
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }

    @DeleteMapping("/profile/delete/{filename}")
    public ResponseEntity<byte[]> delete(@PathVariable("filename") String filename) throws IOException {
        if (profilePicturesService.delete("profile-pics/" + filename)) {
            return ResponseEntity.status(NO_CONTENT).body("File deleted successfully".getBytes());
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }
}
