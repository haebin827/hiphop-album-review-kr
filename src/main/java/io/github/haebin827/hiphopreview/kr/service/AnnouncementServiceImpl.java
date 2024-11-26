package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import io.github.haebin827.hiphopreview.kr.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository ar;

    public void saveAnnouncement(AnnouncementDTO announcementDTO) {
        Announcement announcement = Announcement.builder()
                .title(announcementDTO.getTitle())
                .content(announcementDTO.getContent())
                .isImp(announcementDTO.getIsImp())
                .build();

        ar.save(announcement);
    }

    // 검색 및 목록 조회
    public Page<Announcement> getAnnouncements(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.isEmpty()) {
            // 제목 검색
            return ar.findByTitleContainingIgnoreCase(keyword, pageable);
        }
        // 전체 목록 조회 (isImp 기준)
        return ar.findByIsImpFalseOrderByRegDateDesc(pageable);
    }

    public List<Announcement> getImportantAnnouncements() {
        return ar.findByIsImpTrueOrderByRegDateDesc();
    }

    /*public Page<Announcement> getGeneralAnnouncements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ar.findByIsImpFalseOrderByRegDateDesc(pageable);
    }*/

    public Announcement getAnnouncement(Integer id) {
        return ar.findById(id).orElse(null);
    }

    public void incrementViews(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            // ADMIN이 아닌 경우 조회수 증가
            ar.incrementViews(id);
        }
    }

    public void deleteAnnouncement(Integer id) {
        ar.deleteById(id);
    }

    public void updateAnnouncement(AnnouncementDTO announcementDTO) {
        Announcement announcement = ar.findById(announcementDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid announcement ID"));
        announcement.change(announcementDTO.getTitle(), announcementDTO.getContent(), announcementDTO.getIsImp());
        ar.save(announcement);
    }
}
