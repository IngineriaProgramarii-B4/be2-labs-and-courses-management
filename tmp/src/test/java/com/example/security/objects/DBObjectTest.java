package com.example.security.objects;


import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DBObjectTest {
    @Test
    public void testEquals_ObjectsAreEqual_ReturnsTrue() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        boolean isDeleted = true;

        DBObject obj1 = new DBObject();
        obj1.setCreatedAt(createdAt);
        obj1.setUpdatedAt(updatedAt);
        obj1.setIsDeleted(isDeleted);

        DBObject obj2 = new DBObject();
        obj2.setCreatedAt(createdAt);
        obj2.setUpdatedAt(updatedAt);
        obj2.setIsDeleted(isDeleted);

        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_ObjectsAreEqual_ReturnsFalse() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        boolean isDeleted = true;

        DBObject obj1 = new DBObject();
        obj1.setCreatedAt(createdAt);
        obj1.setUpdatedAt(updatedAt);
        obj1.setIsDeleted(isDeleted);

        DBObject obj2 = new DBObject();
        obj2.setCreatedAt(createdAt);
        obj2.setUpdatedAt(updatedAt);

        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        DBObject dbObject = new DBObject();
        dbObject.setCreatedAt(new Date());
        dbObject.setUpdatedAt(new Date());
        dbObject.setIsDeleted(true);

        assertFalse(dbObject.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        DBObject dbObject = new DBObject();
        dbObject.setCreatedAt(new Date());
        dbObject.setUpdatedAt(new Date());
        dbObject.setDeleted(true);
        Object otherObject = new Object();

        assertFalse(dbObject.equals(otherObject));
    }

    @Test
    public void testEquals_DifferentCreatedAt_ReturnsFalse() {
        Date createdAt1 = new Date();
        Date createdAt2 = new Date();
        Date updatedAt = new Date();

        DBObject dbObject1 = new DBObject();
        dbObject1.setCreatedAt(createdAt1);
        dbObject1.setUpdatedAt(updatedAt);
        dbObject1.setDeleted(true);

        DBObject dbObject2 = new DBObject();
        dbObject2.setCreatedAt(createdAt2);
        dbObject2.setUpdatedAt(updatedAt);

        assertFalse(dbObject1.equals(dbObject2));
    }

    @Test
    public void testEquals_DifferentUpdatedAt_ReturnsFalse() {
        Date createdAt = new Date();
        Date updatedAt1 = new Date();
        Date updatedAt2 = new Date();

        DBObject dbObject1 = new DBObject();
        dbObject1.setCreatedAt(createdAt);
        dbObject1.setUpdatedAt(updatedAt1);
        dbObject1.setDeleted(true);

        DBObject dbObject2 = new DBObject();
        dbObject2.setCreatedAt(createdAt);
        dbObject2.setUpdatedAt(updatedAt2);

        assertFalse(dbObject1.equals(dbObject2));
    }

    @Test
    public void testEquals_DifferentIsDeleted_ReturnsFalse() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        DBObject dbObject1 = new DBObject();
        dbObject1.setCreatedAt(createdAt);
        dbObject1.setUpdatedAt(updatedAt);
        dbObject1.setDeleted(true);

        DBObject dbObject2 = new DBObject();
        dbObject2.setCreatedAt(createdAt);
        dbObject2.setUpdatedAt(updatedAt);
        dbObject2.setDeleted(false);

        assertFalse(dbObject1.equals(dbObject2));
    }

    @Test
    public void testToString_ReturnsExpectedString() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        DBObject dbObject = new DBObject();
        dbObject.setCreatedAt(createdAt);
        dbObject.setUpdatedAt(updatedAt);
        dbObject.setDeleted(true);

        String expectedString = "DBObject{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=true" +
                "}";

        assertEquals(expectedString, dbObject.toString());
    }

    @Test
    public void testHashCode_ReturnsExpectedHashCode() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        DBObject dbObject = new DBObject();
        dbObject.setCreatedAt(createdAt);
        dbObject.setUpdatedAt(updatedAt);
        dbObject.setDeleted(true);

        int expectedHashCode = Objects.hash(createdAt, updatedAt, true);

        assertEquals(expectedHashCode, dbObject.hashCode());
    }

}
