package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.*;
import io.github.haebin827.hiphopreview.kr.domain.artist.Country;
import io.github.haebin827.hiphopreview.kr.dto.ArtistDTO;
import io.github.haebin827.hiphopreview.kr.dto.CountryDTO;
import io.github.haebin827.hiphopreview.kr.repository.ArtistRepository;
import io.github.haebin827.hiphopreview.kr.repository.CountryRepository;
import io.github.haebin827.hiphopreview.kr.util.LocalUploader;
import io.github.haebin827.hiphopreview.kr.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepo;
    private final CountryRepository countryRepo;
    private final ModelMapper mm;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    public ArtistDTO getArtistById(Integer id) {

        Artist artist = artistRepo.findById(id).orElse(null);

        if (artist == null) {
            return null;
        }

        ArtistDTO artistDTO = mm.map(artist, ArtistDTO.class);

        return artistDTO;
    }

    public void saveArtist(ArtistDTO artistDTO) {

        // s3로 이미지 업로드
        String s3Url = null;
        MultipartFile image = artistDTO.getImage();
        String uuid = UUID.randomUUID().toString();

        if (image != null && !image.isEmpty()) {
            try {
                String localFilePath = localUploader.uploadLocal(image, "artist", uuid).get(0);
                s3Url = s3Uploader.upload(localFilePath, "image");
                log.info("S3URL: " + s3Url);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
        }

        List<Country> countries = countryRepo.findAll();

        // artistDTO의 country 값이 존재하지 않는 경우 새 레코드 삽입
        String artistCountry = artistDTO.getCountry();

        boolean countryExists = countries.stream()
                .anyMatch(country -> country.getCountry().equalsIgnoreCase(artistCountry));
        if (!countryExists) {
            Country newCountry = Country.builder()
                    .country(artistCountry)
                    .build();
            countryRepo.save(newCountry); // Country 레코드 저장
            log.info("New country added: " + newCountry.getCountry());
        }

        artistDTO.setUuid(uuid);
        artistDTO.setS3url(s3Url);
        Artist artist = mm.map(artistDTO, Artist.class);

        Artist savedArtist = artistRepo.save(artist);
        log.info("Saved artist: " + savedArtist);
    }

    public Page<ArtistDTO> getArtists(String searchType, String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Artist> artistPage = null;

        if ((keyword != null && !keyword.isEmpty()) && (searchType != null && !searchType.isEmpty())) {
            artistPage = artistRepo.findByCountryAndNameContainingIgnoreCaseOrderByBornedInDesc(searchType, keyword, pageable);
        } else if (searchType != null && !searchType.isEmpty()) {
            artistPage = artistRepo.findByCountryOrderByBornedInDesc(searchType, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            artistPage = artistRepo.findByNameContainingIgnoreCaseOrderByBornedInDesc(keyword, pageable);
        } else{
            artistPage = artistRepo.findAllByOrderByBornedInDesc(pageable);
        }

        return artistPage.map(artist -> {
            ArtistDTO artistDTO = mm.map(artist, ArtistDTO.class);

            // Artist 관련 정보를 추가로 설정
            //if (artist.getAlbums() != null) {
            //    albumDTO.setArtistId(album.getArtist().getId());
            //}
            return artistDTO;
        });
    }

    public int getAge(String bornIn) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthLocalDate = LocalDate.parse(bornIn, formatter);
        LocalDate currentDate = LocalDate.now();

        return Period.between(birthLocalDate, currentDate).getYears();
    }

    public List<CountryDTO> getCountries() {

        List<Country> countries = countryRepo.findAll();

        List<CountryDTO> countryDTOs = countries.stream().map(country -> {
            CountryDTO countryDTO = mm.map(country, CountryDTO.class);
            return countryDTO;
        }).collect(Collectors.toList());

        return countryDTOs;
    }

}
