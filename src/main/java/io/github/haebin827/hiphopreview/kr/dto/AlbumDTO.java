package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private Integer id;

    @NotEmpty(message = "앨범/EP 명은 필수 입력 항목입니다.")
    private String title;

    @NotEmpty(message = "아티스트 명은 필수 입력 항목입니다.")
    private String artistName;

    //private List<ArtistDTO> artists;
    @NotEmpty(message = "구분은 필수 입력 항목입니다.")
    private String primaryType;

    private String secondaryType;

    @Size(min = 4, max = 8)
    private String year;

    private String artistUuid;

    @Min(0)
    @Max(5)
    private float rating;

    private String link;

    private MultipartFile image;

    private boolean isActive;

    private List<ReviewDTO> reviews;
}
