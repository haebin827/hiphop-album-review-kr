package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Optional<Artist> findByUuid(String uuid);
}
