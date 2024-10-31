package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
