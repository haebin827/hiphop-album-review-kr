package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistRequestDTO {

    private Integer id;

    @NotNull
    private Integer userId;

    @NotEmpty(message = "아티스트 명은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "아티스트 성별은 필수 입력 항목입니다.")
    private String gender;

    @NotEmpty(message = "아티스트 유형은 필수 입력 항목입니다.")
    private String type;

    @Pattern(
            regexp = "^(1[0-9]{3}|[2-9][0-9]{3})$|^(1[0-9]{3}|[2-9][0-9]{3})-\\d{2}-\\d{2}$",
            message = "아티스트 출생 연도는 'xxxx' 또는 'xxxx-xx-xx' 형식으로 입력해야 합니다."
    )
    private String bornedIn;

    private String country;

    @Size(max = 5000, message = "태그는 최대 5000자까지 입력 가능합니다.")
    private String tags;

    private MultipartFile image;

    @Size(max = 1000)
    private String note;
}
