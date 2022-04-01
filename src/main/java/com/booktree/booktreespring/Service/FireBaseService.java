package com.booktree.booktreespring.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FireBaseService {
    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    public String uploadFiles(MultipartFile file) throws IOException, FirebaseAuthException {
        String nameFile = UUID.randomUUID().toString() + ".jpg";

        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = new ByteArrayInputStream(file.getBytes());
        Blob blob = bucket.create(nameFile.toString(), content, file.getContentType());
        return blob.getMediaLink();
    }
}
