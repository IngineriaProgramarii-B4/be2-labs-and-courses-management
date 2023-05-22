package com.example.firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/blob")
public class TestController {

    private final FirebaseStorageStrategy firebaseStorageStrategy;

    @Autowired
    public TestController(FirebaseStorageStrategy firebaseStorageStrategy) {
        this.firebaseStorageStrategy = firebaseStorageStrategy;
    }

    @PostMapping
    public ResponseEntity<byte[]> upload(@RequestParam("file")MultipartFile multipartFile) {
        String rootFolder = "test1/test2/test3";
        if (firebaseStorageStrategy.upload(multipartFile, multipartFile.getOriginalFilename(), rootFolder)) {
            return ResponseEntity.status(CREATED).body("File added successfully".getBytes());
        }
        return ResponseEntity.status(OK).body("File not added".getBytes());
    }

    @GetMapping("/filename={filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename) throws IOException {
        byte[] file = firebaseStorageStrategy.download(filename);
        if (file != null) {
            return ResponseEntity.ok().body(file);
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }

    @DeleteMapping("/filename={filename}")
    public ResponseEntity<byte[]> delete(@PathVariable("filename") String filename) throws IOException {
        if (firebaseStorageStrategy.deleteFile(filename)) {
            return ResponseEntity.status(NO_CONTENT).body("File deleted successfully".getBytes());
        }
        return ResponseEntity.status(NOT_FOUND).body("File not found".getBytes());
    }

}
