package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Artist;
import io.github.haebin827.hiphopreview.kr.domain.ArtistRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Integer> {
}
