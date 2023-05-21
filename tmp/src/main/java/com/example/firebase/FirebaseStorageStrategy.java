package com.example.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class FirebaseStorageStrategy{

    private final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/unimanager-8952e.appspot.com/o/%s?alt=media";
    private String TEMP_URL = "";
    private final String firebasePropsPath = "src/main/resources/firebaseProps.json";

    private String uploadFile(File file, String fileName, String rootFolder) throws IOException {
        String unified = rootFolder + "/" + fileName;
        BlobId blobId = BlobId.of("unimanager-8952e.appspot.com", unified);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebasePropsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        byte[] allBytes = Files.readAllBytes(file.toPath());
        storage.create(blobInfo, allBytes);
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    public boolean upload(MultipartFile multipartFile, String fileName, String rootFolder) {
        try {
            File file = this.convertToFile(multipartFile, fileName);
            TEMP_URL = this.uploadFile(file, fileName, rootFolder);
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] download(String fullPath) throws IOException {
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebasePropsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("unimanager-8952e.appspot.com", fullPath));
        return blob.getContent();
    }

    public boolean deleteFile(String fullPath) throws IOException{
        BlobId blobId = BlobId.of("unimanager-8952e.appspot.com", fullPath);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebasePropsPath));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.delete(blobId);
    }

}
