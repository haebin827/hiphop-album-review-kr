package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.dto.ArtistDTO;
import io.github.haebin827.hiphopreview.kr.dto.CountryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArtistService {

    ArtistDTO getArtistById(Integer id);
    void saveArtist(ArtistDTO artistDTO);
    Page<ArtistDTO> getArtists(String searchType, String keyword, int page, int size);
    int getAge(String bornIn);
    List<CountryDTO> getCountries();
}
