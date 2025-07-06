package io.github.haebin827.hiphopreview.kr.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;

    @ToString.Exclude
    @NotNull
    private UserDTO user;

    // 만약 album을 직접 추가한다면, uuid로 albumId가 겹치지 않게 하기
    @NotNull
    private Integer albumId;

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    @Size(min = 1, max = 50, message = "제목은 최대 50자 이하여야 합니다")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    @Size(min = 1, max = 5000, message = "내용은 최대 5000자 이하여야 합니다")
    private String content;

    @NotNull(message = "평점은 필수 입력 항목입니다.")
    @Min(0)
    @Max(5)
    private float rating;

    @Min(0)
    @Max(5)
    private float ratingCompletion;

    @Min(0)
    @Max(5)
    private float ratingCohesion;

    @Min(0)
    @Max(5)
    private float ratingReplayability;

    @Min(0)
    private int likes;

    @Min(0)
    private int dislikes;

    private boolean likedByCurrentUser;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    /*public Integer getUserId() {
        return user.getId();
    }

    public Integer getAlbumId() {
        return album.getId();
    }*/
}
