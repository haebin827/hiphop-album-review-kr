package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnnouncementService {

    void saveAnnouncement(AnnouncementDTO announcementDTO);
    AnnouncementDTO getAnnouncement(Integer id);
    Page<AnnouncementDTO> getAnnouncements(String keyword, int page, int size);
    List<AnnouncementDTO> getImportantAnnouncements();
    //Page<Announcement> getGeneralAnnouncements(int page, int size);
    void incrementViews(Integer id);
    void deleteAnnouncement(Integer id);
    void updateAnnouncement(AnnouncementDTO announcementDTO);
}
