package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
