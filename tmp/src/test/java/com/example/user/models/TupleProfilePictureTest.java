package com.example.user.models;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

class TupleProfilePictureTest {
    private TupleProfilePicture tupleProfilePicture = new TupleProfilePicture("Hello".getBytes(), "png");

    @Test
    void setBytesTest() {

        tupleProfilePicture.setBytes("World".getBytes(StandardCharsets.UTF_8));

        assertEquals(new String(tupleProfilePicture.getBytes(), StandardCharsets.UTF_8), "World");
    }

    @Test
    void setExtensionTest() {

        tupleProfilePicture.setExtension("jpeg");

        assertEquals(tupleProfilePicture.getExtension(), "jpeg");
    }
}
