package com.example.user.service;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.user.models.ProfilePicture;
import com.example.user.models.TupleProfilePicture;
import com.example.user.repository.ProfilePicturesRepository;
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
import java.util.UUID;

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

    @Mock
    ProfilePicturesRepository profilePicturesRepository;

    @Test
    void uploadProfilePictureSuccessTest() {
        //Given
        UUID testId = UUID.randomUUID();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.jpeg",
                "image/jpeg",
                "Test data".getBytes()
        );

        //When
        when(firebaseStorageStrategy.upload(Mockito.any(), anyString(), anyString())).thenReturn(true);
        when(profilePicturesRepository.findProfilePictureByParams(testId)).thenReturn(null);
        when(profilePicturesRepository.findProfilePictureByName("test")).thenReturn(null);

        boolean result = profilePicturesService.uploadProfilePicture(mockFile, testId);

        profilePicturesRepository.deleteByName(mockFile.getOriginalFilename());

        //Then
        assertTrue(result);
    }

    @Test
    void uploadProfilePictureFailureTest() {
        //Given
        UUID testId = UUID.randomUUID();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test",
                "jpeg",
                "Test data".getBytes()
        );

        //When
        when(firebaseStorageStrategy.upload(Mockito.any(), anyString(), anyString())).thenReturn(false);
        when(profilePicturesRepository.findProfilePictureByParams(testId)).thenReturn(null);
        when(profilePicturesRepository.findProfilePictureByName("test")).thenReturn(null);

        boolean result = profilePicturesService.uploadProfilePicture(mockFile, testId);

        //Then
        assertFalse(result);
    }

    @Test
    void downloadFoundTest() throws IOException {
        //Given
        byte[] fileData = "Test file content".getBytes();
        UUID testId = UUID.randomUUID();

        //When
        when(firebaseStorageStrategy.download(anyString())).thenReturn(fileData);
        when(profilePicturesRepository.findProfilePictureByParams(testId)).thenReturn(new ProfilePicture(UUID.randomUUID(), "test", "png"));

        TupleProfilePicture result = profilePicturesService.download(testId);

        //Then
        assertArrayEquals(fileData, result.getBytes());
    }

    @Test
    void downloadNotFoundTest() throws IOException {
        //Given
        UUID testId = UUID.randomUUID();

        //When
        when(profilePicturesRepository.findProfilePictureByParams(testId)).thenReturn(null);

        TupleProfilePicture result = profilePicturesService.download(testId);

        //Then
        assertNull(result);
    }

    @Test
    void deleteSuccessTest() throws IOException {
        //When
        when(firebaseStorageStrategy.deleteFile("profile-pics/" + "test.png")).thenReturn(true);
        doNothing().when(profilePicturesRepository).deleteByName("test");

        boolean result = profilePicturesService.delete("test.png");

        //Then
        assertTrue(result);
    }

    @Test
    void deleteFailureTest() throws IOException {
        //When
        when(firebaseStorageStrategy.deleteFile("profile-pics/" + "test")).thenReturn(false);

        boolean result = profilePicturesService.delete("test");

        //Then
        assertFalse(result);
    }
}
