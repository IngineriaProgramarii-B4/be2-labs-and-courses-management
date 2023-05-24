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

    private byte[] downloadBasedOnExt(String path, String ext) throws IOException {
        String newPath = "profile-pics/" + path + "." + ext;
        try {
            return firebaseStorageStrategy.download(newPath);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public byte[] download(String fullPath) throws IOException {
        byte[] file;
        file = downloadBasedOnExt(fullPath, "png");
        if (file == null) {
            file = downloadBasedOnExt(fullPath, "jpg");
            if (file == null) {
                file = downloadBasedOnExt(fullPath, "jpeg");
                return file;
            }
            return file;
        }
        return file;
    }

    public boolean delete(String filename) throws IOException {
        if (firebaseStorageStrategy.deleteFile("profile-pics/" + filename)) {
            return true;
        }
        return false;
    }

}
