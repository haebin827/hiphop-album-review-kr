package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    // 제목 검색 (페이징 포함)
    Page<Announcement> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 상단 고정 공지사항 (검색 시 사용하지 않음)
    List<Announcement> findByIsImpTrueOrderByRegDateDesc();

    // 일반 공지사항
    Page<Announcement> findByIsImpFalseOrderByRegDateDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Announcement a SET a.views = a.views + 1 WHERE a.id = :id")
    void incrementViews(Integer id);

}
