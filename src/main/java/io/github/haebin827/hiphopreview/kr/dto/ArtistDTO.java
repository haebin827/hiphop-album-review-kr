package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private Integer id;

    @NotEmpty
    private String name;

    private LocalDate bornedIn;

    private String nationality;

    private List<AlbumDTO> albums;
}
