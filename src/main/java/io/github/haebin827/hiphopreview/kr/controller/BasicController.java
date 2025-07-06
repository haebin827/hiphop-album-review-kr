package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.service.AlbumService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BasicController {

    private final AlbumService albumSvc;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        // 세션 확인
        if (session == null) {
            log.info("CHECK: Session does not exist.");
        } else {
            log.info("CHECK: Session exists: {}", session.getId());
        }

        // 인증 정보 확인 (Spring Security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("CHECK: Authenticated user: {}", authentication.getName());
        } else {
            log.info("CHECK: No authenticated user found.");
        }

        session.removeAttribute("tempUser");
        session.removeAttribute("toggle");
        session.removeAttribute("requestSource");
        session.removeAttribute("userEmail");


        List<AlbumDTO> top30NewestAlbums = albumSvc.getTop30NewestAlbums();
        // Partition the list into sublists of 6 albums each
        List<List<AlbumDTO>> partitionedAlbums = ListUtils.partition(top30NewestAlbums, 6);

        model.addAttribute("partitionedAlbums", partitionedAlbums);

        return "home";
    }
}
