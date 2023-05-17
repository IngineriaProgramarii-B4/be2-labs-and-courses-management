package com.example.security.objects;

import jakarta.persistence.MappedSuperclass;

import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public class DBObject {
//    @Column(nullable = false)
    private Date createdAt;
//    @Column(nullable = false)
    private Date updatedAt;
//    @Column(nullable = false)
    private boolean isDeleted = false;


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DBObject setIsDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }
    
        public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBObject dbObject = (DBObject) o;
        return isDeleted == dbObject.isDeleted && Objects.equals(createdAt, dbObject.createdAt) && Objects.equals(updatedAt, dbObject.updatedAt);
    }

    @Override
    public String toString() {
        return "DBObject{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, isDeleted);
    }
}
