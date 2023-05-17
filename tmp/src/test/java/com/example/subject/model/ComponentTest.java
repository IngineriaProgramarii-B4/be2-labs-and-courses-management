package com.example.subject.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    private Component component;

    @BeforeEach
    void setUp(){
        component = new Component();
    }
    @Test
    void setResources() {
        component.setResources(List.of(new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png")));
        assertEquals(1, component.getResources().size());
        assertEquals("Physics_romania.png", component.getResources().get(0).getTitle());
        assertEquals("savedResources/Physics_romania.png", component.getResources().get(0).getLocation());
        assertEquals("image/png", component.getResources().get(0).getType());
        assertFalse(component.getResources().get(0).isDeleted());
    }

    @Test
    void setNumberWeeks() {
        component.setNumberWeeks(10);
        assertEquals(10, component.getNumberWeeks());
    }

    @Test
    void setDeleted() {
        component.setDeleted(true);
        assertTrue(component.isDeleted());
    }

    @Test
    void addResource() {
        component.addResource(new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png"));
        assertEquals(1, component.getResources().size());
        assertEquals("Physics_romania.png", component.getResources().get(0).getTitle());
        assertEquals("savedResources/Physics_romania.png", component.getResources().get(0).getLocation());
        assertEquals("image/png", component.getResources().get(0).getType());
        assertFalse(component.getResources().get(0).isDeleted());
    }

    @Test
    void removeResource() {
        Resource resource = new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png");
        component.addResource(resource);
        assertEquals(1, component.getResources().size());
        assertEquals("Physics_romania.png", component.getResources().get(0).getTitle());
        assertEquals("savedResources/Physics_romania.png", component.getResources().get(0).getLocation());
        assertEquals("image/png", component.getResources().get(0).getType());
        assertFalse(component.getResources().get(0).isDeleted());
        component.removeResource(resource);
        assertEquals(0, component.getResources().size());
    }

    @Test
    void softDeleteResource() {
        Resource resource = new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png");
        component.addResource(resource);
        assertEquals(1, component.getResources().size());
        assertEquals("Physics_romania.png", component.getResources().get(0).getTitle());
        assertEquals("savedResources/Physics_romania.png", component.getResources().get(0).getLocation());
        assertEquals("image/png", component.getResources().get(0).getType());
        assertFalse(component.getResources().get(0).isDeleted());
        component.softDeleteResource(resource);
        assertEquals(1, component.getResources().size());
        assertEquals("Physics_romania.png", component.getResources().get(0).getTitle());
        assertEquals("savedResources/Physics_romania.png", component.getResources().get(0).getLocation());
        assertEquals("image/png", component.getResources().get(0).getType());
        assertTrue(component.getResources().get(0).isDeleted());
    }

    @Test
    void softDeleteResourceNotFound() {
        Resource resource = new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png");
        assertEquals(0, component.getResources().size());

        component.softDeleteResource(resource);
        assertEquals(0, component.getResources().size());
    }

    @Test
    void testToString() {
        component.setResources(List.of(new Resource("Physics_romania.png", "savedResources/Physics_romania.png", "image/png")));
        component.setNumberWeeks(10);
        assertEquals("Component{type='null', numberWeeks=10, " +
                "resources=[Resource{title='Physics_romania.png', location='savedResources/Physics_romania.png', type='image/png'}]}", component.toString());
    }
}