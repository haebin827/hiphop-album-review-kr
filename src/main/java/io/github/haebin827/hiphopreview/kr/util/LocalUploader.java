package io.github.haebin827.hiphopreview.kr.util;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
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

    /*
    MultipartFile 타입의 객체를 받아서 실제로 로컬 폴더에 파일을 저장하고,
    이미지 파일일 경우에는 썸네일을 생성함.
    ==> 둘 다 실제 파일로 저장됨
    */
    public List<String> uploadLocal(MultipartFile file) {

        if(file == null || file.isEmpty()) {
            return null;
        }

        //String uuid = UUID.randomUUID().toString();
        //String saveFileName = uuid + "_" + file.getOriginalFilename();

        Path savePath = Paths.get(uploadPath, file.getOriginalFilename());

        List<String> savePathList = new ArrayList<>();

        try {
            file.transferTo(savePath);

            savePathList.add(savePath.toFile().getAbsolutePath());

            if(Files.probeContentType(savePath).startsWith("image")) {
                File thumbFile = new File(uploadPath, "s_" + file.getOriginalFilename());
                savePathList.add(thumbFile.getAbsolutePath());
                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
            }
        } catch(Exception e) {
            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return savePathList;
    }
}