package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import io.github.haebin827.hiphopreview.kr.domain.Artist;
import io.github.haebin827.hiphopreview.kr.domain.Review;
import io.github.haebin827.hiphopreview.kr.domain.album.AlbumSecondaryType;
import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.dto.AlbumSecondaryTypeDTO;
import io.github.haebin827.hiphopreview.kr.dto.ArtistDTO;
import io.github.haebin827.hiphopreview.kr.repository.AlbumRepository;
import io.github.haebin827.hiphopreview.kr.repository.AlbumSecondaryTypeRepository;
import io.github.haebin827.hiphopreview.kr.repository.ArtistRepository;
import io.github.haebin827.hiphopreview.kr.repository.ReviewRepository;
import io.github.haebin827.hiphopreview.kr.util.LocalUploader;
import io.github.haebin827.hiphopreview.kr.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import java.text.DecimalFormat;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final ArtistRepository artistRepo;
    private final AlbumRepository albumRepo;
    private final ReviewRepository reviewRepo;
    private final AlbumSecondaryTypeRepository secondaryTypeRepo;
    private final ModelMapper mm;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    public List<AlbumDTO> getTop30NewestAlbums() {

        Pageable pageable = PageRequest.of(0, 30); // 30개 제한
        List<Album> albums = albumRepo.findTop30NewestAlbums(pageable);

        List<AlbumDTO> albumDTOs = albums.stream().map(album -> {
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);
            return albumDTO;
        }).collect(Collectors.toList());

        return albumDTOs;
    }

    public void saveAlbum(AlbumDTO albumDTO) {

        // s3로 이미지 업로드
        String s3Url = null;
        MultipartFile image = albumDTO.getImage();
        String uuid = UUID.randomUUID().toString();

        if (image != null && !image.isEmpty()) {
            /*try {
                String localFilePath = localUploader.uploadLocal(image, "album", uuid).get(0);
                s3Url = s3Uploader.upload(localFilePath, "image");
                log.info("S3URL: " + s3Url);
            } catch (Exception e) {
                throw new RuntimeException("AlbumServiceImpl: Failed to upload image to S3 - ", e);
            }*/
        }

        Optional<Artist> optionalArtist = artistRepo.findByUuid(albumDTO.getArtistUuid());
        optionalArtist.ifPresent(artist -> {
            // Artist를 ArtistDTO로 변환
            ArtistDTO artistDTO = mm.map(artist, ArtistDTO.class);
            // albumDTO에 ArtistDTO 설정
            albumDTO.setArtist(artistDTO);
        });
        // Album 엔터티 생성
        albumDTO.setUuid(uuid);
        albumDTO.setS3url(s3Url);
        Album album = mm.map(albumDTO, Album.class);

        log.info("Saved albumDTO: "  + albumDTO);
        Album savedAlbum = albumRepo.save(album);
        log.info("Saved album: " + savedAlbum);
    }

    public Page<AlbumDTO> getAlbums(String searchType, String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Album> albumPage = null;

        if (searchType != null && !searchType.isEmpty() && keyword != null && !keyword.isEmpty()) {
            if(searchType.equals("album")) {
                albumPage = albumRepo.findByTitleContainingIgnoreCaseOrderByYearDesc(keyword, pageable);
            } else if(searchType.equals("artist")) {
                albumPage = albumRepo.findByArtistNameContainingIgnoreCaseOrderByYearDesc(keyword, pageable);
            }
        } else {
            albumPage = albumRepo.findAllByOrderByYearDesc(pageable);
        }

        return albumPage.map(album -> {
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

            if (album.getArtist() != null) {
                ArtistDTO artistDTO = mm.map(album.getArtist(), ArtistDTO.class);
                albumDTO.setArtist(artistDTO);
            }
            return albumDTO;
        });

    }

    public AlbumDTO getAlbumById(Integer id) {

        Album album = albumRepo.findById(id).orElse(null);

        if (album == null) {
            return null;
        }

        AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);

        if (album.getArtist() != null) {
            ArtistDTO artistDTO = mm.map(album.getArtist(), ArtistDTO.class);
            albumDTO.setArtist(artistDTO);
        }

        return albumDTO;
    }

    public List<AlbumDTO> getAlbumsByArtist(Integer artistId) {

        List<Album> albums = albumRepo.findAllByArtistIdOrderByYearDesc(artistId);

        List<AlbumDTO> albumDTOs = albums.stream().map(album -> {
            AlbumDTO albumDTO = mm.map(album, AlbumDTO.class);
            return albumDTO;
        }).collect(Collectors.toList());

        return albumDTOs;
    }

    public HashMap<String, String> getRatingsAndTotalReviews(Integer albumId) {

        Album album = albumRepo.findById(albumId).orElse(null);

        if (album == null) {
            return new HashMap<>();
        }

        List<Review> reviews = album.getReviews();
        int reviewCount = reviews.size();

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

        HashMap<String, String> ratingsAndReviews = new HashMap<>();
        ratingsAndReviews.put("avgRating", dfTwoDecimals.format(averageRating)); // 소숫점 1~2자리로 포맷
        ratingsAndReviews.put("avgRatingCompletion", dfOneDecimal.format(averageRatingCompletion));
        ratingsAndReviews.put("avgRatingCohesion", dfOneDecimal.format(averageRatingCohesion));
        ratingsAndReviews.put("avgRatingReplayability", dfOneDecimal.format(averageRatingReplayability));
        ratingsAndReviews.put("totalReviews", String.valueOf(reviewCount));

        return ratingsAndReviews;
    }

    public List<AlbumSecondaryTypeDTO> getSecondaryTypes() {

        List<AlbumSecondaryType> secondaryTypes = secondaryTypeRepo.findAll();

        List<AlbumSecondaryTypeDTO> secondaryTypeDTOs = secondaryTypes.stream().map(secondaryType -> {
            AlbumSecondaryTypeDTO secondaryTypeDTO = mm.map(secondaryType, AlbumSecondaryTypeDTO.class);
            return secondaryTypeDTO;
        }).collect(Collectors.toList());

        return secondaryTypeDTOs;
    }
}
