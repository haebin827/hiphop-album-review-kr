package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.dto.FileDTO;
import io.github.haebin827.hiphopreview.kr.util.LocalUploader;
import io.github.haebin827.hiphopreview.kr.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
public class FileDtoController {

    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload-form"; // `upload-form.html`이라는 이름의 HTML 템플릿이 필요합니다.
    }

    @PostMapping("/upload")
    public String upload(FileDTO fileDTO, RedirectAttributes redirectAttributes) {
        MultipartFile[] files = fileDTO.getFiles();

        if (files == null || files.length <= 0) {
            redirectAttributes.addFlashAttribute("message", "No files uploaded!");
            return "redirect:/upload";
        }

        List<String> uploadedFilePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));
        }
        log.info("Uploaded local files: " + uploadedFilePaths);

        List<String> s3Paths = uploadedFilePaths.stream()
                .map(fileName -> s3Uploader.upload(fileName))
                .collect(Collectors.toList());

        log.info("Uploaded S3 files: " + s3Paths);

        // Add the S3 paths to the redirect attributes to show after redirection
        redirectAttributes.addFlashAttribute("message", "Files uploaded successfully!");
        redirectAttributes.addFlashAttribute("s3Paths", s3Paths);

        return "redirect:/upload/result";
    }

    @GetMapping("/upload/result")
    public String showUploadResult(Model model) {
        return "upload-result"; // `upload-result.html`이라는 HTML 템플릿이 필요합니다.
    }
}


/*
package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.dto.FileDTO;
import io.github.haebin827.hiphopreview.kr.util.LocalUploader;
import io.github.haebin827.hiphopreview.kr.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class FileDtoController {

    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public List<String> upload(FileDTO fileDTO) {
        MultipartFile[] files = fileDTO.getFiles();

        if(files == null || files.length <= 0) {
            return null;
        }
        List<String> uploadedFilePaths = new ArrayList<>();

        for(MultipartFile file : files) {
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));

        }
        log.info("-----------------------------------------");
        log.info(uploadedFilePaths);

        List<String> s3Paths = uploadedFilePaths.stream().map(fileName -> s3Uploader.upload(fileName)).collect(Collectors.toList());

        return s3Paths;
    }
}
*/
