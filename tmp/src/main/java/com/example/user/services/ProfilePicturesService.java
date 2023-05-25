package com.example.user.services;

import com.example.firebase.FirebaseStorageStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfilePicturesService {

    private final FirebaseStorageStrategy firebaseStorageStrategy;

    public ProfilePicturesService(FirebaseStorageStrategy firebaseStorageStrategy) {
        this.firebaseStorageStrategy = firebaseStorageStrategy;
    }

    public boolean uploadProfilePicture(MultipartFile multipartFile, String originalFilename) {
        String rootFolder = "profile-pics";
        if (firebaseStorageStrategy.upload(multipartFile, originalFilename, rootFolder)) {
            return true;
        }
        return false;
    }


    public byte[] download(String path) throws IOException {
        // TODO: de cautat extensia din db

//        path = "profile-pics/" + path + "." + extensie;
//        byte[] file;
//        file = firebaseStorageStrategy.download(path);

//        return file;
        return null;
    }

    public boolean delete(String filename) throws IOException {
        if (firebaseStorageStrategy.deleteFile("profile-pics/" + filename)) {
            return true;
        }
        return false;
    }

}
