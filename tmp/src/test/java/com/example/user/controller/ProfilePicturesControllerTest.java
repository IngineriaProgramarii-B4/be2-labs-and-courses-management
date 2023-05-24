package com.example.user.controller;

import com.example.user.controllers.ProfilePicturesController;
import com.example.user.services.ProfilePicturesService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProfilePicturesControllerTest {

    @InjectMocks
    private ProfilePicturesController profilePicturesController;

    @Mock
    private ProfilePicturesService profilePicturesService;

    @Test
    void uploadProfilePictureSuccessTest() throws Exception {
        //Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(profilePicturesService.uploadProfilePicture(mockFile, mockFile.getOriginalFilename()))
                .thenReturn(true);

        ResponseEntity<byte[]> response = profilePicturesController.uploadProfilePicture(mockFile);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void uploadProfilePictureFailureTest() {
        //Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(profilePicturesService.uploadProfilePicture(mockFile, mockFile.getOriginalFilename()))
                .thenReturn(false);

        ResponseEntity<byte[]> response = profilePicturesController.uploadProfilePicture(mockFile);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void downloadFileFoundTest() throws IOException {
        //Given
        byte[] fileData = "Test file content".getBytes();

        //When
        when(profilePicturesService.download("test.jpg")).thenReturn(fileData);

        ResponseEntity<byte[]> response = profilePicturesController.download("test.jpg");

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void downloadFileNotFoundTest() throws IOException {
        //When
        when(profilePicturesService.download("test.jpg")).thenReturn(null);

        ResponseEntity<byte[]> response = profilePicturesController.download("test.jpg");

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteFileFoundTest() throws IOException {
        //When
        when(profilePicturesService.delete("profile-pics/test.jpg")).thenReturn(true);

        ResponseEntity<byte[]> response = profilePicturesController.delete("test.jpg");

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteFileNotFoundTest() throws IOException {
        //When
        when(profilePicturesService.delete("profile-pics/test.jpg")).thenReturn(false);

        ResponseEntity<byte[]> response = profilePicturesController.delete("test.jpg");

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}