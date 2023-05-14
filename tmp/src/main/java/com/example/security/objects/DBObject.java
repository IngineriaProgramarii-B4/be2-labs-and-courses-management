package com.example.security.objects;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;

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

    public DBObject setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
