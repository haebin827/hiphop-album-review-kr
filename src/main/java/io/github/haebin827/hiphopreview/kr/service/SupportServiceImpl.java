package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.*;
import io.github.haebin827.hiphopreview.kr.dto.ArtistRequestDTO;
import io.github.haebin827.hiphopreview.kr.dto.FeedbackDTO;
import io.github.haebin827.hiphopreview.kr.dto.ReviewDTO;
import io.github.haebin827.hiphopreview.kr.repository.ArtistRequestRepository;
import io.github.haebin827.hiphopreview.kr.repository.FeedbackRepository;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import io.github.haebin827.hiphopreview.kr.util.LocalUploader;
import io.github.haebin827.hiphopreview.kr.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SupportServiceImpl implements SupportService{

    private final FeedbackRepository fr;
    private final ArtistRequestRepository ar;
    private final UserRepository ur;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    public void saveFeedback(FeedbackDTO feedbackDTO, Integer userId) {
        // User 엔티티 조회
        User user = ur.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Feedback 엔터티 생성
        Feedback feedback = Feedback.builder()
                .category(feedbackDTO.getCategory())
                .content(feedbackDTO.getContent())
                .user(user) // User 설정
                .build();

        // 피드백 저장
        Feedback savedFeedback = fr.save(feedback);
        log.info("Saved feedback: " + savedFeedback);
    }

    public void saveArtistRequest(ArtistRequestDTO artistRequestDTO, Integer userId) {

        // s3로 이미지 업로드
        String s3Url = null;
        MultipartFile image = artistRequestDTO.getImage();

        if (image != null && !image.isEmpty()) {
            try {
                String localFilePath = localUploader.uploadLocal(image, "artistrequest").get(0);
                s3Url = s3Uploader.upload(localFilePath, "image");
                log.info("S3URL: " + s3Url);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        // User 엔티티 조회
        User user = ur.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Feedback 엔터티 생성
        ArtistRequest artistRequest = ArtistRequest.builder()
                .name(artistRequestDTO.getName())
                .type(artistRequestDTO.getType())
                .gender(artistRequestDTO.getGender())
                .bornedIn(artistRequestDTO.getBornedIn())
                .country(artistRequestDTO.getCountry())
                .tags(artistRequestDTO.getTags())
                .note(artistRequestDTO.getNote())
                .s3url(s3Url)
                .user(user)
                .build();

        // 피드백 저장
        ArtistRequest savedArtistRequest = ar.save(artistRequest);
        log.info("Saved feedback: " + savedArtistRequest);
    }
}
