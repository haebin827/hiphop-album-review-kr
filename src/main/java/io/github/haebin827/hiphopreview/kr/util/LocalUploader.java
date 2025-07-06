package io.github.haebin827.hiphopreview.kr.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class LocalUploader {

    @Value("${albumreview.upload.path}")
    private String uploadPath;

    private final Tika tika = new Tika();

    /**
     * MultipartFile 타입의 객체를 받아서 로컬 폴더에 파일을 저장하고,
     * 이미지 파일일 경우 썸네일을 생성.
     */

    public List<String> uploadLocal(MultipartFile file, String type, String uuid) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("LocalUploader: Uploaded file is null or empty");
        }

        String saveFileName = type + "_" + uuid;
        //String saveFileName = type + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadPath, saveFileName);

        List<String> savePathList = new ArrayList<>();

        try {
            // 파일 저장
            file.transferTo(savePath.toFile());
            savePathList.add(savePath.toFile().getAbsolutePath());

            // Tika를 사용하여 MIME 타입 확인
            String contentType = tika.detect(savePath.toFile());
            log.info("Detected content type: " + contentType);

            // 이미지 파일인 경우 썸네일 생성
            /*if (contentType.startsWith("image")) {
                File thumbFile = new File(uploadPath, "s_" + file.getOriginalFilename());
                savePathList.add(thumbFile.getAbsolutePath());
                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
            } else {
                log.warn("File is not an image: " + contentType);
            }*/
        } catch (IOException e) {
            log.error("LocalUploader: I/O Error while saving file or creating thumbnail - " + e.getMessage(), e);
            throw new RuntimeException("File operation failed: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("LocalUploader: Invalid argument provided - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("LocalUploader: Unexpected error - " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while uploading the file", e);
        }
        return savePathList;
    }
}
