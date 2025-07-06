package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumInterviewDTO {
    
    private Integer id;

    @NotEmpty
    private String albumUuid;

    @NotEmpty
    private String type;

    @NotEmpty
    private String url;

    private LocalDateTime regDate;
}