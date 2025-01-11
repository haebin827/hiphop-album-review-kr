package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.dto.ReviewDTO;
import org.springframework.data.domain.Page;

public interface ReviewService {

    ReviewDTO saveReview(ReviewDTO reviewDTO);
    Page<ReviewDTO> getReviews(int page, int size, Integer id, String sort, Integer userId);
    boolean hasUserReviewedAlbum(Integer userId, Integer albumId);
}
