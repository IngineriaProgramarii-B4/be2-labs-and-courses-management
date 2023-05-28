package com.example.user.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfilePictureTest {
    private ProfilePicture profilePicture;

    @BeforeEach
    public void setup()
    {
        profilePicture = new ProfilePicture();
    }
    @Test
    void testSetId()
    {
        //
        //Given
        //
        UUID newId = UUID.randomUUID();

        //
        //When
        //
        profilePicture.setId(newId);

        //
        //Then
        //
        assertEquals(newId, profilePicture.getId());
    }

    @Test
    void testSetUserId()
    {
        //
        //Given
        //
        UUID newId = UUID.randomUUID();

        //
        //When
        //
        profilePicture.setUserId(newId);

        //
        //Then
        //
        assertEquals(newId, profilePicture.getUserId());
    }

    @Test
    void testSetPictureName()
    {
        //
        //Given
        //
        String name = "testname";
        //
        //When
        //
        profilePicture.setPictureName(name);

        //
        //Then
        //
        assertEquals(name, profilePicture.getPictureName());
    }

    @Test
    void testSetPictureExtension()
    {
        //
        //Given
        //
        String ext = "testextension";
        //
        //When
        //
        profilePicture.setPictureExtension(ext);

        //
        //Then
        //
        assertEquals(ext, profilePicture.getPictureExtension());
    }

    @Test
    void testCtor1()
    {
        //
        //Given
        //
        profilePicture = new ProfilePicture(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "testname",
                "testextension"
        );

        //
        //When
        //
        profilePicture.setPictureName("changed_name");

        //
        //Then
        //
        assertEquals("changed_name", profilePicture.getPictureName());
    }

    @Test
    void testCtor2()
    {
        //
        //Given
        //
        profilePicture = new ProfilePicture(
                UUID.randomUUID(),
                "testname",
                "testextension"
        );

        //
        //When
        //
        profilePicture.setPictureName("changed_name");

        //
        //Then
        //
        assertEquals("changed_name", profilePicture.getPictureName());
    }
}