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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SupportServiceImpl implements SupportService{

    private final FeedbackRepository feedbackRepo;
    private final ArtistRequestRepository artistReqRepo;
    private final UserRepository userRepo;
    private final ModelMapper mm;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    public void saveFeedback(FeedbackDTO feedbackDTO, Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Feedback feedback = Feedback.builder()
                .category(feedbackDTO.getCategory())
                .content(feedbackDTO.getContent())
                .user(user)
                .build();

        Feedback savedFeedback = feedbackRepo.save(feedback);
        log.info("Saved feedback: " + savedFeedback);
    }

    public void saveArtistRequest(ArtistRequestDTO artistRequestDTO, Integer userId) {

        // s3로 이미지 업로드
        String s3Url = null;
        MultipartFile image = artistRequestDTO.getImage();
        String uuid = UUID.randomUUID().toString();

        if (image != null && !image.isEmpty()) {
            try {
                String localFilePath = localUploader.uploadLocal(image, "artistrequest", uuid).get(0);
                s3Url = s3Uploader.upload(localFilePath, "image");
                log.info("S3URL: " + s3Url);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        artistRequestDTO.setUuid(uuid);
        artistRequestDTO.setS3url(s3Url);
        artistRequestDTO.setUser(user);

        ArtistRequest artistRequest = mm.map(artistRequestDTO, ArtistRequest.class);
        ArtistRequest savedArtistRequest = artistReqRepo.save(artistRequest);
        log.info("Saved ArtistRequest: " + savedArtistRequest);
    }
}
