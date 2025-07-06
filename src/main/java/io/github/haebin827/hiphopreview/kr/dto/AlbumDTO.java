package io.github.haebin827.hiphopreview.kr.dto;

import io.github.haebin827.hiphopreview.kr.domain.Artist;
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

    private String artistUuid;

    private ArtistDTO artist;

    @NotEmpty(message = "구분은 필수 입력 항목입니다.")
    private String primaryType;

    private String secondaryType;

    @Pattern(
            regexp = "^(1[0-9]{3}|[2-9][0-9]{3})$|^(1[0-9]{3}|[2-9][0-9]{3})-\\d{2}-\\d{2}$",
            message = "발매일은 'xxxx' 또는 'xxxx-xx-xx' 형식으로 입력해야 합니다."
    )
    private String year;

    private String youtubeUrl;

    private String soundcloudUrl;

    private String uuid;

    private String s3url;

    private MultipartFile image;

    private boolean isActive;

    private List<ReviewDTO> reviews;
}
