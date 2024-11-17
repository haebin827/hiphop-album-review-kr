package io.github.haebin827.hiphopreview.kr.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique file name to avoid overwriting existing files
        String fileName = file.getOriginalFilename();

        // Set metadata for the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload the file to S3
        s3Client.putObject("haebin-albumreview-images", fileName, file.getInputStream(), metadata);

        // Return the file URL
        return s3Client.getUrl("haebin-albumreview-images", fileName).toString();
    }
}
