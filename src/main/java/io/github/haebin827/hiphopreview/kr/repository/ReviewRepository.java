package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Page<Review> findAllByAlbumIdOrderByRatingDescLikesDesc(Integer albumId, Pageable pageable);

    Page<Review> findAllByAlbumIdOrderByRatingAscLikesDesc(Integer albumId, Pageable pageable);

    Page<Review> findAllByAlbumIdOrderByRegDateDesc(Integer albumId, Pageable pageable);

    Page<Review> findAllByAlbumIdOrderByRegDateAsc(Integer albumId, Pageable pageable);

    Page<Review> findAllByAlbumIdOrderByLikesDesc(Integer albumId, Pageable pageable);

    Page<Review> findAllByAlbumIdAndUserId(Integer albumId, Integer userId, Pageable pageable);

    @Query("SELECT COALESCE(AVG(r.rating), 0), COUNT(r) FROM Review r WHERE r.album.id = :albumId")
    Object[] getRatingAndTotalReviews(@Param("albumId") Integer albumId);

    boolean existsByUserIdAndAlbumId(Integer userId, Integer albumId);
    List<Review> findAllByAlbumId(Integer albumId);


}
