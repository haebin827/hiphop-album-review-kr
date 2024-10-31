package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private Integer id;

    @NotEmpty
    private String title;

    @NotEmpty
    private List<ArtistDTO> artists;

    @NotNull
    private float rating;

    private boolean isActive;

    private List<ReviewDTO> reviews;
}
