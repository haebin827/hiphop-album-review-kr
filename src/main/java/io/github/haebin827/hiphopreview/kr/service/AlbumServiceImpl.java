package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.repository.AlbumRepository;
import io.github.haebin827.hiphopreview.kr.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import java.text.DecimalFormat;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository ar;
    private final ReviewRepository rr;
    private final ModelMapper mm;

    public List<AlbumDTO> getTop30NewestAlbums() {

        Pageable pageable = PageRequest.of(0, 30); // 30개 제한
        List<Album> albums = ar.findTop30NewestAlbums(pageable);

        // Album 엔티티를 AlbumDTO로 매핑
        List<AlbumDTO> albumDTOs = albums.stream().map(album -> {
            // 기본 필드는 ModelMapper로 매핑
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

            // artistName과 artistUuid를 추가로 설정
            if (album.getArtist() != null) {
                albumDTO.setArtistId(album.getArtist().getId());
                albumDTO.setArtistName(album.getArtist().getName());
                albumDTO.setArtistUuid(album.getArtist().getUuid());
                albumDTO.setArtistBornedIn(album.getArtist().getBornedIn());
                albumDTO.setArtistCountry(album.getArtist().getCountry());
                albumDTO.setArtistType(album.getArtist().getType());
                albumDTO.setArtistTag(album.getArtist().getTags());
            }

            return albumDTO;
        }).collect(Collectors.toList());

        return albumDTOs;
    }

    public Page<AlbumDTO> getAlbums(String searchType, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 엔티티 페이지 조회
        Page<Album> albumPage = null;
        if (searchType != null && !searchType.isEmpty() && keyword != null && !keyword.isEmpty()) {
            if(searchType.equals("album")) {
                albumPage = ar.findByTitleContainingIgnoreCaseOrderByYearDesc(keyword, pageable);
            } else if(searchType.equals("artist")) {
                albumPage = ar.findByArtistNameContainingIgnoreCaseOrderByYearDesc(keyword, pageable);
            }
        } else {
            albumPage = ar.findAllByOrderByYearDesc(pageable);
        }

        // 엔티티를 DTO로 변환하며 추가 작업 수행
        return albumPage.map(album -> {
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

            // Artist 관련 정보를 추가로 설정
            if (album.getArtist() != null) {
                albumDTO.setArtistId(album.getArtist().getId());
                albumDTO.setArtistName(album.getArtist().getName());
                albumDTO.setArtistUuid(album.getArtist().getUuid());
                albumDTO.setArtistBornedIn(album.getArtist().getBornedIn());
                albumDTO.setArtistCountry(album.getArtist().getCountry());
                albumDTO.setArtistType(album.getArtist().getType());
                albumDTO.setArtistTag(album.getArtist().getTags());
            }

            return albumDTO;
        });
    }

    public AlbumDTO getAlbumById(Integer id) {
        Album album = ar.findById(id)
                .orElse(null); // 앨범이 없으면 null 반환

        if (album == null) {
            return null;
        }

        // Album -> AlbumDTO 매핑
        AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

        // Artist 정보 추가
        if (album.getArtist() != null) {
            albumDTO.setArtistId(album.getArtist().getId());
            albumDTO.setArtistName(album.getArtist().getName());
            albumDTO.setArtistUuid(album.getArtist().getUuid());
            albumDTO.setArtistBornedIn(album.getArtist().getBornedIn());
            albumDTO.setArtistCountry(album.getArtist().getCountry());
            albumDTO.setArtistType(album.getArtist().getType());
            albumDTO.setArtistTag(album.getArtist().getTags());
        }

        return albumDTO;
    }

    public List<AlbumDTO> getAlbumsByArtist(Integer artistId) {

        List<Album> albums = ar.findAllByArtistIdOrderByYearDesc(artistId);

        // Album 엔티티를 AlbumDTO로 변환
        List<AlbumDTO> albumDTOs = albums.stream().map(album -> {
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

            return albumDTO;
        }).collect(Collectors.toList());

        return albumDTOs;
    }

    public HashMap<String, String> getRatingsAndTotalReviews(Integer albumId) {
        Album album = ar.findById(albumId).orElse(null);

        // Album이 없으면 빈 HashMap 반환
        if (album == null) {
            return new HashMap<>();
        }

        // Album에 연결된 리뷰 가져오기
        List<Review> reviews = album.getReviews();

        // 리뷰 수 확인
        int reviewCount = reviews.size();

        // 리뷰가 없는 경우
        if (reviewCount == 0) {
            HashMap<String, String> result = new HashMap<>();
            result.put("avgRating", "0.0");
            result.put("avgRatingCompletion", "0");
            result.put("avgRatingCohesion", "0");
            result.put("avgRatingReplayability", "0");
            result.put("totalReviews", "0");
            return result;
        }

        // 리뷰의 평점 합계 계산
        float totalRating = 0.0f;
        float totalRatingCompletion = 0f;
        float totalRatingCohesion = 0f;
        float totalRatingReplayability = 0f;

        for (Review review : reviews) {
            totalRating += review.getRating();
            totalRatingCompletion += review.getRatingCompletion();
            totalRatingCohesion += review.getRatingCohesion();
            totalRatingReplayability += review.getRatingReplayability();
        }

        // 평균 평점 계산
        float averageRating = totalRating / reviewCount;
        float averageRatingCompletion = totalRatingCompletion / reviewCount;
        float averageRatingCohesion = totalRatingCohesion / reviewCount;
        float averageRatingReplayability = totalRatingReplayability / reviewCount;

        // DecimalFormat으로 포맷팅 (소숫점 1~2자리, .0 유지)
        DecimalFormat dfTwoDecimals = new DecimalFormat("0.0#"); // 평균 평점 소숫점 2자리
        DecimalFormat dfOneDecimal = new DecimalFormat("0.#");   // 나머지 소숫점 1자리

        // 결과 HashMap에 저장
        HashMap<String, String> ratingsAndReviews = new HashMap<>();
        ratingsAndReviews.put("avgRating", dfTwoDecimals.format(averageRating)); // 소숫점 1~2자리로 포맷
        ratingsAndReviews.put("avgRatingCompletion", dfOneDecimal.format(averageRatingCompletion));
        ratingsAndReviews.put("avgRatingCohesion", dfOneDecimal.format(averageRatingCohesion));
        ratingsAndReviews.put("avgRatingReplayability", dfOneDecimal.format(averageRatingReplayability));
        ratingsAndReviews.put("totalReviews", String.valueOf(reviewCount));

        return ratingsAndReviews;
    }

}
