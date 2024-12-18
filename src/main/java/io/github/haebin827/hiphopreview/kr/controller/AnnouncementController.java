package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.domain.Announcement;
import io.github.haebin827.hiphopreview.kr.dto.AnnouncementDTO;
import io.github.haebin827.hiphopreview.kr.repository.AnnouncementRepository;
import io.github.haebin827.hiphopreview.kr.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/announce")
@Log4j2
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService as;

    /*@GetMapping("/list")
    public void listGET(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {

        log.info("PAGE: " + page);
        log.info("SIZE: " + size);

        // 중요한 공지사항 목록
        List<Announcement> importantAnnouncements = as.getImportantAnnouncements();

        // 일반 공지사항 페이징 처리
        Page<Announcement> generalAnnouncements = as.getGeneralAnnouncements(page, size);

        // 한 번에 보여줄 페이지 번호의 수 (10개)
        int pageSize = 10;
        int currentGroupStart = (page / pageSize) * pageSize; // 현재 페이지 그룹의 시작 번호
        int currentGroupEnd = Math.min(currentGroupStart + pageSize - 1, generalAnnouncements.getTotalPages() - 1); // 그룹의 끝 번호

        // **1부터 시작하는 인덱스 계산**
        int startIndex = page * size + 1;

        // 데이터 모델에 추가
        model.addAttribute("importantAnnouncements", importantAnnouncements); // 중요한 공지사항
        model.addAttribute("announcements", generalAnnouncements.getContent()); // 일반 공지사항
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", generalAnnouncements.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);
        model.addAttribute("startIndex", startIndex);
    }*/

    @GetMapping("/list")
    public void listGET(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {

        log.info("PAGE: " + page);
        log.info("SIZE: " + size);
        log.info("KEYWORD: " + keyword);

        Page<Announcement> announcements = as.getAnnouncements(keyword, page, size);

        // 기본 목록 데이터
        if (keyword == null || keyword.isEmpty()) {
            List<Announcement> importantAnnouncements = as.getImportantAnnouncements();
            model.addAttribute("importantAnnouncements", importantAnnouncements);
        }

        int pageSize = 10;
        int currentGroupStart = (page / pageSize) * pageSize;
        int currentGroupEnd = Math.min(currentGroupStart + pageSize - 1, announcements.getTotalPages() - 1);
        int startIndex = page * size + 1;

        log.info("CURRENT GROUP START: " + currentGroupStart);
        log.info("CURRENT GROUP END: " + currentGroupEnd);
        log.info("TOTAL PAGES: " + announcements.getTotalPages());
        model.addAttribute("announcements", announcements.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", announcements.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("keyword", keyword); // 검색어 전달
    }


    //나중에 이거 활성화해 @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public void newGET() {

    }

    @PostMapping("/new")
    public String newPOST(@Valid AnnouncementDTO announcementDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/announce/new";
        }

        log.info("ANNOUNCEMENT: " + announcementDTO);

        as.saveAnnouncement(announcementDTO);

        redirectAttributes.addFlashAttribute("result", "Registered");
        return "redirect:/announce/list";
    }

    @GetMapping("/read")
    public void readGET(@RequestParam("id") Integer id,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        Model model) {

        // 조회수 증가
        as.incrementViews(id);

        // 공지사항 데이터
        Announcement announcement = as.getAnnouncement(id);
        if (announcement == null) {
            throw new IllegalArgumentException("Invalid announcement ID: " + id);
        }

        model.addAttribute("announcement", announcement);
        model.addAttribute("page", page);
    }

    @GetMapping("/edit")
    public void editGET(@RequestParam("id") Integer id, Model model) {
        Announcement announcement = as.getAnnouncement(id);
        if (announcement == null) {
            throw new IllegalArgumentException("Invalid announcement ID: " + id);
        }
        model.addAttribute("announcement", announcement);
    }

    @PostMapping("/edit")
    public String editPOST(@ModelAttribute AnnouncementDTO announcementDTO, RedirectAttributes redirectAttributes) {
        try {
            as.updateAnnouncement(announcementDTO);
            redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            log.error("Error updating announcement with ID " + announcementDTO.getId(), e);
            redirectAttributes.addFlashAttribute("error", "공지사항 수정 중 문제가 발생했습니다.");
        }
        return "redirect:/announce/read?id=" + announcementDTO.getId();
    }


    @PostMapping("/delete")
    public String deletePOST(@RequestParam("id") Integer id,
                             @RequestParam("page") int page, // 페이지 번호를 받아옴
                             RedirectAttributes redirectAttributes) {
        try {
            as.deleteAnnouncement(id);
        } catch (Exception e) {
            log.error("Error deleting announcement with ID " + id, e);
            redirectAttributes.addFlashAttribute("error", "공지사항 삭제 중 문제가 발생했습니다.");
            return "redirect:/announce/read?id=" + id + "&page=" + page;
        }
        return "redirect:/announce/list?page=" + page;
    }
}
