package com.example.user.service;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.user.services.ProfilePicturesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProfilePicturesServiceTest {
    @InjectMocks
    ProfilePicturesService profilePicturesService;

    @Mock
    FirebaseStorageStrategy firebaseStorageStrategy;

    @Test
    void uploadProfilePictureSuccessTest() {
        //Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpeg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(firebaseStorageStrategy.upload(Mockito.any(), anyString(), anyString())).thenReturn(true);

        boolean result = profilePicturesService.uploadProfilePicture(mockFile, "test.jpg");

        //Then
        assertTrue(result);
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
        when(firebaseStorageStrategy.upload(Mockito.any(), anyString(), anyString())).thenReturn(false);

        boolean result = profilePicturesService.uploadProfilePicture(mockFile, "test.jpg");

        //Then
        assertFalse(result);
    }

    @Test
    void downloadFoundPngTest() throws IOException {
        //Given
        byte[] fileData = "Test file content".getBytes();

        //When
        when(firebaseStorageStrategy.download(anyString())).thenReturn(fileData);

        byte[] result = profilePicturesService.download("test");

        //Then
        assertArrayEquals(fileData, result);
    }

    @Test
    void downloadFoundJpgTest2() throws IOException {
        //Given
        byte[] fileData = "Test file content".getBytes();

        //When
        when(firebaseStorageStrategy.download("profile-pics/test.jpg"))
                .thenReturn(fileData);

        when(firebaseStorageStrategy.download("profile-pics/test.png"))
                .thenThrow(NullPointerException.class);

        byte[] result = profilePicturesService.download("test");

        //Then
        assertArrayEquals(fileData, result);
    }

    @Test
    void downloadNotFoundTest() throws IOException {
        //When
        when(firebaseStorageStrategy.download(anyString())).thenThrow(NullPointerException.class);

        byte[] result = profilePicturesService.download("test.jpg");

        //Then
        assertNull(result);
    }

    @Test
    void deleteSuccessTest() throws IOException {
        //When
        when(firebaseStorageStrategy.deleteFile(anyString())).thenReturn(true);

        boolean result = profilePicturesService.delete("test.jpg");

        //Then
        assertTrue(result);
    }

    @Test
    void deleteFailureTest() throws IOException {
        //When
        when(firebaseStorageStrategy.deleteFile(anyString())).thenReturn(false);

        boolean result = profilePicturesService.delete("test.jpg");

        //Then
        assertFalse(result);
    }
}
