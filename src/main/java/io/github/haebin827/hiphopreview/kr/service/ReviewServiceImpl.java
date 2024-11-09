package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.dto.ReviewDTO;
import io.github.haebin827.hiphopreview.kr.repository.AlbumRepository;
import io.github.haebin827.hiphopreview.kr.repository.ReviewRepository;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements  ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final ModelMapper mm;

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + reviewDTO.getUser().getId()));

        Album album = albumRepository.findById(reviewDTO.getAlbum().getId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found with id: " + reviewDTO.getAlbum().getId()));

        Review review = mm.map(reviewDTO, Review.class);

        review.setUser(user);
        review.setAlbum(album);

        Review savedReview = reviewRepository.save(review);

        return mm.map(savedReview, ReviewDTO.class);
    }
}
