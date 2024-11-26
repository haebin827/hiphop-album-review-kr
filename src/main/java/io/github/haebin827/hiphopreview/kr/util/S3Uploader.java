package io.github.haebin827.hiphopreview.kr.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final Tika tika = new Tika();

    @Value("${cloud.aws.s3.bucket.albums}")
    public String imageBucket;

    @Value("${cloud.aws.s3.bucket.profilepics}")
    public String profileBucket;

    // S3로 파일 업로드 (목적에 따라 버킷 선택)
    public String upload(String filePath, String bucketType) {
        log.info("S3 UPLOADER................");
        File targetFile = new File(filePath);
        String bucketName = resolveBucket(bucketType);

        String fileNameWithExtension = generateFileNameWithExtension(targetFile);
        String uploadImageUrl = putS3(targetFile, bucketName, fileNameWithExtension);
        removeOriginalFile(targetFile);

        return uploadImageUrl;
    }

    // 버킷 이름 선택
    private String resolveBucket(String bucketType) {
        log.info("S3 resolve Bucket type: " + bucketType);

        switch (bucketType) {
            case "user":
                return profileBucket;
            case "image":
                return imageBucket;
            default:
                throw new IllegalArgumentException("Invalid bucket type: " + bucketType);
        }
    }

    // 확장자가 포함된 파일 이름 생성
    private String generateFileNameWithExtension(File file) {
        String originalName = file.getName();
        String extension = "";

        try {
            // Tika로 파일 MIME 타입 감지
            String mimeType = tika.detect(file);
            log.info("Detected MIME Type: " + mimeType);

            // 확장자 매핑
            switch (mimeType) {
                case "image/jpeg":
                    extension = ".jpg";
                    break;
                case "image/png":
                    extension = ".png";
                    break;
                case "image/gif":
                    extension = ".gif";
                    break;
                case "image/bmp":
                    extension = ".bmp";
                    break;
                default:
                    extension = ".bin"; // 기본 확장자 설정
                    break;
            }
        } catch (IOException e) {
            log.error("Failed to detect MIME type: " + e.getMessage());
        }

        if (!originalName.contains(".")) {
            return originalName + extension; // 기존 이름에 확장자 추가
        }
        return originalName; // 이미 확장자가 있으면 그대로 사용
    }

    // S3로 업로드
    private String putS3(File uploadFile, String bucketName, String fileName) {
        try {
            // MIME 타입 감지
            String mimeType = tika.detect(uploadFile);
            log.info("Detected MIME Type for S3 upload: " + mimeType);

            // 메타데이터에 Content-Type 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mimeType);

            // S3 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
                    .withMetadata(metadata);

            amazonS3Client.putObject(putObjectRequest);

            log.info("S3 PUTS3: " + amazonS3Client.getUrl(bucketName, fileName).toString());
            return amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            log.error("Error while detecting MIME type: " + e.getMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    // S3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }

    // S3 파일 삭제
    public void removeS3File(String bucketType, String fileName) {
        String bucketName = resolveBucket(bucketType);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}


/*
package io.github.haebin827.hiphopreview.kr.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket.albums}")
    public String imageBucket;

    @Value("${cloud.aws.s3.bucket.profilepics}")
    public String profileBucket;

    */
/*
    LocalUploader에서 저장했던 실제 이미지 파일 (원본 파일, 썸네일 파일)을 S3 버킷에 업로드.
    업로드가 완료되면 removeOriginalFile() 메서드를 통해 로컬에 저장된 실제 파일 (원본 파일, 썸네일 파일)을 삭제.
    *//*


    // S3로 파일 업로드 (목적에 따라 버킷 선택)
    public String upload(String filePath, String bucketType) throws RuntimeException {
        log.info("S3 UPLOADER................");
        File targetFile = new File(filePath);
        String bucketName = resolveBucket(bucketType);

        String uploadImageUrl = putS3(targetFile, bucketName, targetFile.getName());
        removeOriginalFile(targetFile);

        return uploadImageUrl;
    }

    // 버킷 이름 선택
    private String resolveBucket(String bucketType) {
        log.info("S3 resolve Bucket type: " + bucketType);

        switch (bucketType) {
            case "user":
                return profileBucket;
            case "image":
                return imageBucket;
            default:
                throw new IllegalArgumentException("Invalid bucket type: " + bucketType);
        }
    }

    // S3로 업로드
    private String putS3(File uploadFile, String bucketName, String fileName) throws RuntimeException {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("S3 PUTS3: " + amazonS3Client.getUrl(bucketName, fileName).toString());
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    // S3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }

    // S3 파일 삭제
    public void removeS3File(String bucketType, String fileName) {
        String bucketName = resolveBucket(bucketType);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}
*/
