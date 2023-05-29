package com.example.user.repository;

import com.example.user.models.ProfilePicture;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfilePicturesRepository extends JpaRepository<ProfilePicture, UUID> {

    @Query("select a from ProfilePicture a where a.userId = ?1")
    ProfilePicture findProfilePictureByParams(UUID uuid);

    @Transactional
    @Modifying
    @Query("delete from ProfilePicture a where a.pictureName = ?1")
    void deleteByName(String filename);

    @Query("select a from ProfilePicture a where a.pictureName = ?1")
    ProfilePicture findProfilePictureByName(String s);
}
