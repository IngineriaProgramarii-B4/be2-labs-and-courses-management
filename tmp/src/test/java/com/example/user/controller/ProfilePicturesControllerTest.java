package com.example.user.controller;

import com.example.user.controllers.ProfilePicturesController;
import com.example.user.models.TupleProfilePicture;
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
import java.util.UUID;

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
        UUID testId = UUID.randomUUID();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(profilePicturesService.uploadProfilePicture(mockFile, testId))
                .thenReturn(true);

        ResponseEntity<byte[]> response = profilePicturesController.uploadProfilePicture(testId, mockFile);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void uploadProfilePictureFailureTest() {
        //Given
        UUID testId = UUID.randomUUID();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(profilePicturesService.uploadProfilePicture(mockFile, testId))
                .thenReturn(false);

        ResponseEntity<byte[]> response = profilePicturesController.uploadProfilePicture(testId, mockFile);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void downloadFileFoundTest() throws IOException {
        //Given
        byte[] fileData = "Test file content".getBytes();
        UUID testId = UUID.randomUUID();

        //When
        when(profilePicturesService.download(testId)).thenReturn(new TupleProfilePicture(fileData, null));

        ResponseEntity<byte[]> response = profilePicturesController.download(testId);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void downloadFileNotFoundTest() throws IOException {
        //Given
        UUID testId = UUID.randomUUID();

        //When
        when(profilePicturesService.download(testId)).thenReturn(null);

        ResponseEntity<byte[]> response = profilePicturesController.download(testId);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteFileFoundTest() throws IOException {
        //When
        when(profilePicturesService.delete("test")).thenReturn(true);

        ResponseEntity<byte[]> response = profilePicturesController.delete("test");

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