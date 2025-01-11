package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

public interface AlbumService {

    Page<AlbumDTO> getAlbums(String searchType, String keyword, int page, int size);
    List<AlbumDTO> getTop30NewestAlbums();
    AlbumDTO getAlbumById(Integer id);
    List<AlbumDTO> getAlbumsByArtist(Integer artistId);
    HashMap<String, String> getRatingsAndTotalReviews(Integer albumId);
}
