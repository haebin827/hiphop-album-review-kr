package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Like;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.dto.ReviewDTO;
import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.repository.AlbumRepository;
import io.github.haebin827.hiphopreview.kr.repository.ReviewRepository;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository rr;
    private final UserRepository ur;
    private final AlbumRepository ar;
    private final ModelMapper mm;

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        User user = ur.findById(reviewDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + reviewDTO.getUser().getId()));

        Album album = ar.findById(reviewDTO.getAlbumId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found with id: " + reviewDTO.getAlbumId()));

        Review review = mm.map(reviewDTO, Review.class);
        review.setUser(user);
        review.setAlbum(album);

        Review savedReview = rr.save(review);

        return mm.map(savedReview, ReviewDTO.class);
    }

    public Page<ReviewDTO> getReviews(int page, int size, Integer id, String sort, Integer userId) {
        Pageable pageable = PageRequest.of(page, size);

        // 엔티티 페이지 조회
        Page<Review> reviewPage = null;

        switch (sort) {
            case "popular":
                reviewPage = rr.findAllByAlbumIdOrderByLikesDesc(id, pageable);
                break;
            case "newest":
                reviewPage = rr.findAllByAlbumIdOrderByRegDateDesc(id, pageable);
                break;
            case "oldest":
                reviewPage = rr.findAllByAlbumIdOrderByRegDateAsc(id, pageable);
                break;
            case "highest":
                reviewPage = rr.findAllByAlbumIdOrderByRatingDescLikesDesc(id, pageable);
                break;
            case "lowest":
                reviewPage = rr.findAllByAlbumIdOrderByRatingAscLikesDesc(id, pageable);
                break;
        }

        if(sort.equals("my")) {
           reviewPage = rr.findAllByAlbumIdAndUserId(id, userId, pageable);
        }

        // 엔티티를 DTO로 변환하며 추가 작업 수행
        return reviewPage.map(review -> {
            ReviewDTO reviewDTO = mm.map(review, ReviewDTO.class);

            // Artist 관련 정보를 추가로 설정
            /*if (review.getArtist() != null) {
                albumDTO.setArtistId(album.getArtist().getId());
                albumDTO.setArtistName(album.getArtist().getName());
                albumDTO.setArtistUuid(album.getArtist().getUuid());
                albumDTO.setArtistBornedIn(album.getArtist().getBornedIn());
                albumDTO.setArtistCountry(album.getArtist().getCountry());
                albumDTO.setArtistType(album.getArtist().getType());
                albumDTO.setArtistTag(album.getArtist().getTags());
            }*/

            return reviewDTO;
        });
    }

    public boolean hasUserReviewedAlbum(Integer userId, Integer albumId) {
        return rr.existsByUserIdAndAlbumId(userId, albumId);
    }
}
