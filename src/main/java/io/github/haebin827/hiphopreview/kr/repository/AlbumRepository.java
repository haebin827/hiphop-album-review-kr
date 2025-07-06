package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

    @Query("SELECT a FROM Album a ORDER BY a.year DESC")
    List<Album> findTop30NewestAlbums(Pageable pageable);

    Page<Album> findByTitleContainingIgnoreCaseOrderByYearDesc(String title, Pageable pageable);

    @Query("SELECT a FROM Album a JOIN a.artist ar WHERE LOWER(ar.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY a.year DESC")
    Page<Album> findByArtistNameContainingIgnoreCaseOrderByYearDesc(@Param("name") String name, Pageable pageable);

    Page<Album> findAllByOrderByYearDesc(Pageable pageable);

    @Query("SELECT a FROM Album a WHERE a.artist.id = :artistId ORDER BY a.year DESC")
    List<Album> findAllByArtistIdOrderByYearDesc(@Param("artistId") Integer artistId);


}
