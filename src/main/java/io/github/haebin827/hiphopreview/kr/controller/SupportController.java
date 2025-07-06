package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.dto.*;
import io.github.haebin827.hiphopreview.kr.service.ArtistService;
import io.github.haebin827.hiphopreview.kr.service.SupportService;
import io.github.haebin827.hiphopreview.kr.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/support")
@Log4j2
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportSvc;
    private final ArtistService artistSvc;

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
        if (authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            log.info("현재 로그인된 사용자: " + authentication.getName());
        } else {
            log.info("현재 사용자는 로그인되어 있지 않습니다.");
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
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
    public void artistRequestGET(Model model) {

        List<CountryDTO> countries = artistSvc.getCountries();
        model.addAttribute("countries", countries);
    }

    @PostMapping("/artist-request")
    public String artistRequestPOST(@Valid @ModelAttribute ArtistRequestDTO artistRequestDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        log.info("ARTIST REQUEST: " + artistRequestDTO);
        log.info("UPLOADED FILE: " + artistRequestDTO.getImage());

        if(bindingResult.hasErrors()) {
            log.info("[ERROR] ARTIST REQUEST");
            log.info(bindingResult.getAllErrors().toString());
            return "support/artist-request";
        }

        /*MultipartFile[] files = fileDTO.getFiles();

        if (files != null) {
            List<String> uploadedFilePaths = new ArrayList<>();
            for (MultipartFile file : files) {
                uploadedFilePaths.addAll(localUploader.uploadLocal(file, "artistrequest"));
            }
            log.info("UPLOADED LOCAL FILES: " + uploadedFilePaths);
            List<String> s3Paths = uploadedFilePaths.stream()
                    .map(fileName -> s3Uploader.upload(fileName, "image"))
                    .collect(Collectors.toList());

            log.info("UPLOADED S3 FILES: " + s3Paths);
        }*/

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        log.info("USERID: " + userId);

        // 요청 저장
        try {
            supportSvc.saveArtistRequest(artistRequestDTO, userId);
        } catch (Exception e) {
            log.info("FAIL TO SAVE ARTIST REQUEST");
            return "support/artist-request"; // Return to the form page with an error
        }

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/support/artist-request";
    }

    @GetMapping("/album-request")
    public void albumRequestGET() {}

    @GetMapping("/feedback")
    public void feedbackGET(Model model) {
        model.addAttribute("feedbackDTO", new FeedbackDTO()); // FeedbackDTO 객체 추가
    }

    @PostMapping("/feedback")
    public String feedbackPOST(@Valid @ModelAttribute("feedbackDTO") FeedbackDTO feedbackDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        // 1: 오류 리포트
        // 2: 기능 추가
        // 3: 기타

        log.info("FEEDBACK: " + feedbackDTO);
        if (bindingResult.hasErrors()) {
            return "support/feedback"; // 유효성 검증 실패 시 폼으로 돌아감
        }

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        log.info("USERID: " + userId);
        // 피드백 저장
        supportSvc.saveFeedback(feedbackDTO, userId);

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/support/feedback";
    }

    @GetMapping("/terms")
    private void termsGET() {}
}
