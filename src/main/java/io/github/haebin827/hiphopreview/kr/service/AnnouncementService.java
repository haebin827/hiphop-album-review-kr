package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnnouncementService {

    void saveAnnouncement(AnnouncementDTO announcementDTO);
    Announcement getAnnouncement(Integer id);
    Page<Announcement> getAnnouncements(String keyword, int page, int size);
    List<Announcement> getImportantAnnouncements();
    //Page<Announcement> getGeneralAnnouncements(int page, int size);
    void incrementViews(Integer id);
    void deleteAnnouncement(Integer id);
    void updateAnnouncement(AnnouncementDTO announcementDTO);
}
