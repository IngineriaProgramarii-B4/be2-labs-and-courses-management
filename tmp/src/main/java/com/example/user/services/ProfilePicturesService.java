package com.example.user.services;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.user.models.ProfilePicture;
import com.example.user.models.TupleProfilePicture;
import com.example.user.repository.ProfilePicturesRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProfilePicturesService {

    private final FirebaseStorageStrategy firebaseStorageStrategy;
    private final ProfilePicturesRepository profilePicturesRepository;

    public ProfilePicturesService(FirebaseStorageStrategy firebaseStorageStrategy, ProfilePicturesRepository profilePicturesRepository) {
        this.firebaseStorageStrategy = firebaseStorageStrategy;
        this.profilePicturesRepository = profilePicturesRepository;
    }

    public boolean uploadProfilePicture(MultipartFile multipartFile, UUID idUser) {
        String rootFolder = "profile-pics";
        String[] arr = multipartFile.getOriginalFilename().split("\\.");

        ProfilePicture pictureFromUser = profilePicturesRepository.findProfilePictureByParams(idUser);

        if(pictureFromUser != null) {
            try {
                delete(pictureFromUser.getPictureName() + "." + pictureFromUser.getPictureExtension());
            } catch (IOException e) {
                return false;
            }
        }

        ProfilePicture pictureFromOtherUsers = profilePicturesRepository.findProfilePictureByName(arr[0]);

        if(pictureFromOtherUsers != null) {
            try {
                delete(pictureFromOtherUsers.getPictureName() + "." + pictureFromOtherUsers.getPictureExtension());
            } catch (IOException e) {
                return false;
            }
        }

        if (firebaseStorageStrategy.upload(multipartFile, multipartFile.getOriginalFilename(), rootFolder)) {
            profilePicturesRepository.save(new ProfilePicture(idUser, arr[0], arr[1]));
            return true;
        }
        return false;
    }

    public TupleProfilePicture download(UUID idUser) throws IOException {
         ProfilePicture profilePicture = profilePicturesRepository.findProfilePictureByParams(idUser);

         if(profilePicture != null) {
             String path = "profile-pics/" + profilePicture.getPictureName() + "." + profilePicture.getPictureExtension();

             byte[] file;
             file = firebaseStorageStrategy.download(path);

             return new TupleProfilePicture(file, profilePicture.getPictureExtension());
         }

         return null;
    }

    public boolean delete(String filename) throws IOException {
        if (firebaseStorageStrategy.deleteFile("profile-pics/" + filename)) {
            String[] arr = filename.split("\\.");
            profilePicturesRepository.deleteByName(arr[0]);
            return true;
        }
        return false;
    }
}
