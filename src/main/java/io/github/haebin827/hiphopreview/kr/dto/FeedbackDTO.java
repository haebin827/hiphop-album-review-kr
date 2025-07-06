package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Integer id;

    @NotNull(message = "분류는 필수 입력 항목입니다.")
    private int category;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    @Size(min = 1, max = 1000, message = "내용은 최대 1000자 이하여야 합니다")
    private String content;

    private Integer userId;

    private LocalDateTime regDate;
}
