package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<Artist> findByUuid(String uuid);

    Page<Artist> findByCountryAndNameContainingIgnoreCaseOrderByBornedInDesc(String country, String keyword, Pageable pageable);

    Page<Artist> findByCountryOrderByBornedInDesc(String country, Pageable pageable);

    Page<Artist> findByNameContainingIgnoreCaseOrderByBornedInDesc(String keyword, Pageable pageable);

    Page<Artist> findAllByOrderByBornedInDesc(Pageable pageable);
}
