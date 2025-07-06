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


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final AlbumRepository albumRepo;
    private final ModelMapper mm;

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        User user = userRepo.findById(reviewDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + reviewDTO.getUser().getId()));

        Album album = albumRepo.findById(reviewDTO.getAlbumId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found with id: " + reviewDTO.getAlbumId()));

        Review review = mm.map(reviewDTO, Review.class);
        review.setUser(user);
        review.setAlbum(album);

        Review savedReview = reviewRepo.save(review);

        return mm.map(savedReview, ReviewDTO.class);
    }

    public Page<ReviewDTO> getReviews(int page, int size, Integer id, String sort, Integer userId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviewPage = null;

        switch (sort) {
            case "popular":
                reviewPage = reviewRepo.findAllByAlbumIdOrderByLikesDesc(id, pageable);
                break;
            case "newest":
                reviewPage = reviewRepo.findAllByAlbumIdOrderByRegDateDesc(id, pageable);
                break;
            case "oldest":
                reviewPage = reviewRepo.findAllByAlbumIdOrderByRegDateAsc(id, pageable);
                break;
            case "highest":
                reviewPage = reviewRepo.findAllByAlbumIdOrderByRatingDescLikesDesc(id, pageable);
                break;
            case "lowest":
                reviewPage = reviewRepo.findAllByAlbumIdOrderByRatingAscLikesDesc(id, pageable);
                break;
            case "my":
                reviewPage = reviewRepo.findAllByAlbumIdAndUserId(id, userId, pageable);
                break;
            default:
                    reviewPage = reviewRepo.findAllByAlbumIdOrderByLikesDesc(id, pageable);
        }

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

        return reviewRepo.existsByUserIdAndAlbumId(userId, albumId);
    }
}
