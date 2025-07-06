package io.github.haebin827.hiphopreview.kr.controller;

import io.github.haebin827.hiphopreview.kr.domain.Artist;
import io.github.haebin827.hiphopreview.kr.dto.AlbumDTO;
import io.github.haebin827.hiphopreview.kr.dto.ArtistDTO;
import io.github.haebin827.hiphopreview.kr.dto.ArtistRequestDTO;
import io.github.haebin827.hiphopreview.kr.dto.CountryDTO;
import io.github.haebin827.hiphopreview.kr.service.AlbumService;
import io.github.haebin827.hiphopreview.kr.service.ArtistService;
import io.github.haebin827.hiphopreview.kr.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/artist")
@Log4j2
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService as;
    private final AlbumService abs;

    @Value("${cloud.aws.s3.bucket.albums}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @GetMapping("/list")
    public void listGET(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "28") int size,
                        @RequestParam(value = "searchType", required = false) String searchType,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {

        log.info("PAGE: " + page);
        log.info("SIZE: " + size);
        log.info("KEYWORD: " + keyword);

        String artist_default_image = "https://" + bucket + ".s3." + region + ".amazonaws.com/album_default_image.png";

        Page<ArtistDTO> artists = as.getArtists(searchType, keyword, page, size);
        List<CountryDTO> countries = as.getCountries();

        int pageSize = 10;
        int currentGroupStart = (page / pageSize) * pageSize;
        int currentGroupEnd = Math.min(currentGroupStart + pageSize - 1, artists.getTotalPages() - 1);
        int startIndex = page * size + 1;

        log.info("CURRENT GROUP START: " + currentGroupStart);
        log.info("CURRENT GROUP END: " + currentGroupEnd);
        log.info("TOTAL PAGES: " + artists.getTotalPages());
        model.addAttribute("artists", artists.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", artists.getTotalPages());
        model.addAttribute("currentGroupStart", currentGroupStart);
        model.addAttribute("currentGroupEnd", currentGroupEnd);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("artist_default_image", artist_default_image);
        model.addAttribute("countries", countries);
    }

    @GetMapping("/info")
    public void infoGET(@RequestParam("id") Integer id,
                        Model model) {
        log.info("infoGET for artist ID: " + id);

        ArtistDTO artistDTO = as.getArtistById(id);
        if (artistDTO == null) {
           log.warn("Artist not found for ID: " + id);
           //return "redirect:/album/list";
        }

        List<AlbumDTO> albums = abs.getAlbumsByArtist(id);
        if(artistDTO.getType().equals("Person")) {
            int age = as.getAge(artistDTO.getBornedIn());
            model.addAttribute("age", age);
        }

        model.addAttribute("artist", artistDTO);
        model.addAttribute("albums", albums);
    }

    @GetMapping("/new")
    public void newGET(Model model) {

        List<CountryDTO> countries = as.getCountries();
        model.addAttribute("countries", countries);
    }

    @PostMapping("/new")
    public String newPOST(@Valid @ModelAttribute ArtistDTO artistDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        log.info("ARTIST: " + artistDTO);
        log.info("UPLOADED FILE: " + artistDTO.getImage());


        if(bindingResult.hasErrors()) {
            log.info("[ERROR] ARTIST REQUEST");
            log.info(bindingResult.getAllErrors().toString());
            return "artist/new";
        }

        // 요청 저장
        try {
            as.saveArtist(artistDTO);
        } catch (Exception e) {
            log.info("FAIL TO SAVE ARTIST");
            return "artist/new"; // Return to the form page with an error
        }

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/artist/new";
    }

}
