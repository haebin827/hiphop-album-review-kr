package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import io.github.haebin827.hiphopreview.kr.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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

    private final AnnouncementRepository announceRepo;
    private final ModelMapper mm;

    public void saveAnnouncement(AnnouncementDTO announcementDTO) {

        Announcement announcement = Announcement.builder()
                .title(announcementDTO.getTitle())
                .content(announcementDTO.getContent())
                .isImp(announcementDTO.getIsImp())
                .build();

        announceRepo.save(announcement);
    }

    public Page<AnnouncementDTO> getAnnouncements(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcementPage;

        if (keyword != null && !keyword.isEmpty()) {
            announcementPage = announceRepo.findByTitleContainingIgnoreCase(keyword, pageable);
        } else {
            announcementPage = announceRepo.findByIsImpFalseOrderByRegDateDesc(pageable);
        }

        return announcementPage.map(announcement -> mm.map(announcement, AnnouncementDTO.class));
    }

    public List<AnnouncementDTO> getImportantAnnouncements() {

        List<Announcement> importantAnnouncements = announceRepo.findByIsImpTrueOrderByRegDateDesc();

        return importantAnnouncements.stream()
                .map(announcement -> mm.map(announcement, AnnouncementDTO.class))
                .toList(); // Java 16 이상 사용 시 toList(), 그렇지 않으면 Collectors.toList() 사용
    }

    /*public Page<Announcement> getGeneralAnnouncements(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ar.findByIsImpFalseOrderByRegDateDesc(pageable);
    }*/

    public AnnouncementDTO getAnnouncement(Integer id) {

        return announceRepo.findById(id)
                .map(announcement -> mm.map(announcement, AnnouncementDTO.class))
                .orElse(null);
    }

    public void incrementViews(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            // ADMIN이 아닌 경우 조회수 증가
            announceRepo.incrementViews(id);
        }
    }

    public void deleteAnnouncement(Integer id) {
        announceRepo.deleteById(id);
    }

    public void updateAnnouncement(AnnouncementDTO announcementDTO) {

        Announcement announcement = announceRepo.findById(announcementDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid announcement ID"));
        announcement.change(announcementDTO.getTitle(), announcementDTO.getContent(), announcementDTO.getIsImp());

        announceRepo.save(announcement);
    }
}
