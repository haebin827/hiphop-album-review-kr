package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/support")
@Log4j2
@RequiredArgsConstructor
public class SupportController {

    @GetMapping("/devlog")
    public void devlogGET() {}

    @GetMapping("/about")
    public void aboutGET(Model model) {
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 권한 목록 확인
        boolean isUser = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains("ROLE_USER");

        // Model에 데이터 추가
        model.addAttribute("isUser", isUser);

        // 인증된 사용자 정보가 있으면 로그인 상태
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            log.info("현재 로그인된 사용자: " + authentication.getName());
        } else {
            log.info("현재 사용자는 로그인되어 있지 않습니다.");
        }

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String name = userDetails.getName();
            String nickname = userDetails.getNickname();
            log.info("Authenticated User Name: {}", name);
            log.info("Authenticated User Nickname: {}", nickname);
        }

    }

    @GetMapping("/faq")
    public void faqGET() {}

    @GetMapping("/artist-request")
    public void artistRequestGET() {}

    @GetMapping("/album-request")
    public void albumRequestGET() {}

    @GetMapping("/feedback")
    public void feedbackGET() {}

    @GetMapping("/terms")
    private void termsGET() {}
}
