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
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDTO {

    private Integer id;

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;

    private Boolean isImp;

    private int views;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
