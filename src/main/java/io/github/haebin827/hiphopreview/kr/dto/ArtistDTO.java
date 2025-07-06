package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private Integer id;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    //@NotEmpty(message = "성별은 필수 입력 항목입니다.")
    private String gender;

    @NotEmpty(message = "유형은 필수 입력 항목입니다.")
    private String type;

    @Pattern(
            regexp = "^(1[0-9]{3}|[2-9][0-9]{3})$|^(1[0-9]{3}|[2-9][0-9]{3})-\\d{2}-\\d{2}$",
            message = "태어난 해는 'xxxx' 또는 'xxxx-xx-xx' 형식으로 입력해야 합니다."
    )
    private String bornedIn;

    private String country;

    private String tags;

    private MultipartFile image;

    private String s3url;

    private String instaId;

    private String twitterId;

    private String uuid;

    //private List<String> albumNames;

    //private List<AlbumDTO> albums;

}
