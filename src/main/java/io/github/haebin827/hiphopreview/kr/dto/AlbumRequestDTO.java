package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequestDTO {

    private Integer id;

    @NotNull
    private Integer userId;

    @NotEmpty(message = "앨범/EP 명은 필수 입력 항목입니다.")
    private String title;

    @NotEmpty(message = "아티스트 명은 필수 입력 항목입니다.")
    private String artist;

    @NotEmpty(message = "아티스트 고유번호는 필수 입력 항목입니다.")
    private String artistUuid;

    // 년도만 입력도 가능
    @NotEmpty(message = "발매 날짜는 필수 입력 항목입니다.")
    private String releasedDate;

    @Size(max = 1000)
    private String note;

    private MultipartFile image;
}
