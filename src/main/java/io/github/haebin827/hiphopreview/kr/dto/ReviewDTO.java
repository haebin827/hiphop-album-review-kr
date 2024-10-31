package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;

    @NotNull
    private UserDTO user;

    @NotNull
    private AlbumDTO album;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private float rating;

    private LocalDateTime modDate;

    private boolean isDeleted;
}
