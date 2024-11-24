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
    public String bucket;

    @Value("${cloud.aws.s3.bucket.profilepics}")
    public String profileBucket;

    /*
    LocalUploader에서 저장했던 실제 이미지 파일 (원본 파일, 썸네일 파일)을 S3 버킷에 업로드.
    업로드가 완료되면 removeOriginalFile() 메서드를 통해 로컬에 저장된 실제 파일 (원본 파일, 썸네일 파일)을 삭제.
    */

    // S3로 파일 업로드 (목적에 따라 버킷 선택)
    public String upload(String filePath, String bucketType) throws RuntimeException {
        File targetFile = new File(filePath);
        String bucketName = resolveBucket(bucketType);

        String uploadImageUrl = putS3(targetFile, bucketName, targetFile.getName());
        removeOriginalFile(targetFile);

        return uploadImageUrl;
    }

    // 버킷 이름 선택
    private String resolveBucket(String bucketType) {
        switch (bucketType) {
            case "user":
                return profileBucket;
            case "album":
                return bucket;
            default:
                throw new IllegalArgumentException("Invalid bucket type: " + bucketType);
        }
    }

    // S3로 업로드
    private String putS3(File uploadFile, String bucketName, String fileName) throws RuntimeException {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
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
