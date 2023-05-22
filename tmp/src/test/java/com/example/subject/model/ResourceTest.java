package com.example.subject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    private Resource resource;

    @BeforeEach
    void setUp(){
        resource = new Resource();
    }

    @Test
    void setTitle() {
        resource.setTitle("Physics_romania.png");
        assertEquals("Physics_romania.png", resource.getTitle());
    }

    @Test
    void setLocation() {
        resource.setLocation("savedResources/Physics_romania.png");
        assertEquals("savedResources/Physics_romania.png", resource.getLocation());
    }

    @Test
    void setType() {
        resource.setType("image/png");
        assertEquals("image/png", resource.getType());
    }

    @Test
    void setDeleted() {
        resource.setDeleted(false);
        assertFalse(resource.getIsDeleted());
    }

    @Test
    void testToString() {
        resource.setTitle("Physics_romania.png");
        resource.setLocation("savedResources/Physics_romania.png");
        resource.setType("image/png");
        assertEquals("Resource{title='Physics_romania.png', location='savedResources/Physics_romania.png', type='image/png'}", resource.toString());
    }
}