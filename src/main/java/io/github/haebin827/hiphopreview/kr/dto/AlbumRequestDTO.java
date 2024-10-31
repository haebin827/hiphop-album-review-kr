package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequestDTO {
    private Integer id;

    @NotEmpty
    private UserDTO user;

    @NotEmpty
    private String title;

    @NotEmpty
    private String artist;

    @NotEmpty
    private LocalDate releasedDate;

    private String note;

    private boolean isProcessed;
}
