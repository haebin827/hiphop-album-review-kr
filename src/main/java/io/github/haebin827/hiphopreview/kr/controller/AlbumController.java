package io.github.haebin827.hiphopreview.kr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.haebin827.hiphopreview.kr.dto.*;
import io.github.haebin827.hiphopreview.kr.service.AlbumService;
import io.github.haebin827.hiphopreview.kr.service.ReviewService;
import io.github.haebin827.hiphopreview.kr.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/album")
@Log4j2
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService as;
    private final ReviewService rs;

    @Value("${cloud.aws.s3.bucket.albums}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @GetMapping("/list")
    public void listGET(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "28") int size,
                        @RequestParam(value = "searchType", defaultValue = "album") String searchType,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {

        String album_default_image = "https://" + bucket + ".s3." + region + ".amazonaws.com/album_default_image.png";

        Page<AlbumDTO> albums = as.getAlbums(searchType, keyword, page, size);

        int pageSize = 10;
        int currentGroupStart = (page / pageSize) * pageSize;
        int currentGroupEnd = Math.min(currentGroupStart + pageSize - 1, albums.getTotalPages() - 1);
        int startIndex = page * size + 1;

        log.info("CURRENT GROUP START: " + currentGroupStart);
        log.info("CURRENT GROUP END: " + currentGroupEnd);
        log.info("TOTAL PAGES: " + albums.getTotalPages());
        model.addAttribute("albums", albums.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", albums.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword); // 검색어 전달
        model.addAttribute("album_default_image", album_default_image);
    }

    @GetMapping("/info")
    public void infoGET(@RequestParam("id") Integer id,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(defaultValue = "popular") String sort,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        Model model) {

        AlbumDTO albumDTO = as.getAlbumById(id);
        if (albumDTO == null) {
            log.warn("Album not found for ID: " + id);
            //return "redirect:/album/list"; // 앨범이 없으면 목록으로 리다이렉트
        }

        // 현재 로그인한 유저의 ID 가져오기
        boolean hasReviewed = false; // 기본값을 false로 설정
        Integer userId = null;
        // 현재 로그인한 유저의 ID 확인
        if (userDetails != null) {
            userId = userDetails.getId();
            hasReviewed = rs.hasUserReviewedAlbum(userId, id);
        }

        List<AlbumDTO> relatedAlbums = as.getAlbumsByArtist(albumDTO.getArtist().getId());
        Page<ReviewDTO> reviews = rs.getReviews(page, size, id, sort, userId);
        HashMap<String, String> ratingsAndTotalReviews = as.getRatingsAndTotalReviews(id);


        int pageSize = 10;
        int currentGroupStart = (page / pageSize) * pageSize;
        int currentGroupEnd = Math.min(currentGroupStart + pageSize - 1, reviews.getTotalPages() - 1);
        int startIndex = page * size + 1;

        //각 어워드는 1, 2, 3등 메달 아이콘으로 변경
        Map<String, String> awards = new HashMap<>();
        awards.put("가장 영감받은 앨범", "1");
        awards.put("삶에 영향을 미친 앨범", "2");

        //인터뷰가 글인 경우에는 글 아이콘으로 변경
        Map<String, String> interviews = new HashMap<>();
        interviews.put("MASK IS OFF: CHROMAKOPIA", "https://www.youtube.com/watch?v=KvR_QlXaAfU");
        interviews.put("Tyler, the Creator: The Most Honest Version Of Himself Creates ‘Chromakopia’ | Billboard Cover", "https://www.youtube.com/watch?v=xFmXVcUUcvc&t=329s");
        interviews.put("Tyler, The Creator - CHROMAKOPIA Live at Camp Flog Gnaw 2024", "https://www.youtube.com/watch?v=XutD0u4W2p8");

        String album_default_image = "https://" + bucket + ".s3." + region + ".amazonaws.com/album_default_image.png";

        log.info("CURRENT GROUP START: " + currentGroupStart);
        log.info("CURRENT GROUP END: " + currentGroupEnd);
        log.info("TOTAL PAGES: " + reviews.getTotalPages());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviews.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("sort", sort);

        model.addAttribute("hasReviewed", hasReviewed);
        model.addAttribute("reviews", reviews.getContent());
        model.addAttribute("ratingsAndTotalReviews", ratingsAndTotalReviews);
        model.addAttribute("album_default_image", album_default_image);
        model.addAttribute("relatedAlbums", relatedAlbums);
        model.addAttribute("awards", awards);
        model.addAttribute("interviews", interviews);
        model.addAttribute("album", albumDTO);
    }

    @GetMapping("/new")
    public void newGET(Model model) {
        List<AlbumSecondaryTypeDTO> secondaryTypes = as.getSecondaryTypes();

        model.addAttribute("secondaryTypes", secondaryTypes);

    }

    @PostMapping("/new")
    public String newPOST(@Valid @ModelAttribute AlbumDTO albumDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        log.info("ALBUM: " + albumDTO);

        if(bindingResult.hasErrors()) {
            log.info("[ERROR] ALBUM UPLOAD");
            log.info(bindingResult.getAllErrors().toString());
            return "album/new";
        }

        // 요청 저장
        try {
            as.saveAlbum(albumDTO);
        } catch (Exception e) {
            log.info("FAIL TO SAVE ALBUM: " + e);
            return "album/new"; // Return to the form page with an error
        }

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/album/new";
    }

    @GetMapping("/review/new")
    public String reviewNewGET(@RequestParam("id") Integer id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {

        // 현재 로그인한 유저의 ID 가져오기
        Integer userId = userDetails.getId();

        // 이미 리뷰를 작성했는지 확인
        boolean hasReviewed = rs.hasUserReviewedAlbum(userId, id);
        if (hasReviewed) {
            return "redirect:/album/info?id=" + id;
        }

        AlbumDTO albumDTO = as.getAlbumById(id);
        if (albumDTO == null) {
            log.warn("Album not found for ID: " + id);
        }

        model.addAttribute("album", albumDTO);
        return "album/review/new";
    }

    @PostMapping("/review/new")
    public String reviewNewPost(@ModelAttribute ReviewDTO reviewDTO,
                                @RequestParam("albumId") Integer albumId) {
        log.info("Incoming ReviewDTO: " + reviewDTO);

        // 앨범 ID 설정
        reviewDTO.setAlbumId(albumId);

        // 현재 로그인된 유저 정보 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userDetails.getId()); // UserDTO에 필요한 ID 설정
            userDTO.setNickname(userDetails.getUsername());
            reviewDTO.setUser(userDTO);
        } else {
            log.warn("No authenticated user found.");
            return "redirect:/album/new?id=" + albumId;
        }

        // Review 저장 처리
        try {
            ReviewDTO savedReview = rs.saveReview(reviewDTO);
            log.info("Saved ReviewDTO: " + savedReview);
        } catch (IllegalArgumentException e) {
            log.error("Error saving review: " + e.getMessage());
            return "redirect:/album/info?id=" + albumId; // 실패 시 앨범 페이지로 리다이렉트
        }

        return "redirect:/album/info?id=" + albumId;
    }

}
