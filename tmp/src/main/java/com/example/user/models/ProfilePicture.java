package com.example.user.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "profile_pictures")
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private String pictureName;
    private String pictureExtension;

    public ProfilePicture() {
    }

    public ProfilePicture(UUID id, UUID userId, String pictureName, String pictureExtension) {
        this.id = id;
        this.userId = userId;
        this.pictureName = pictureName;
        this.pictureExtension = pictureExtension;
    }

    public ProfilePicture(UUID userId, String pictureName, String pictureExtension) {
        this.userId = userId;
        this.pictureName = pictureName;
        this.pictureExtension = pictureExtension;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureExtension() {
        return pictureExtension;
    }

    public void setPictureExtension(String pictureExtension) {
        this.pictureExtension = pictureExtension;
    }
}
