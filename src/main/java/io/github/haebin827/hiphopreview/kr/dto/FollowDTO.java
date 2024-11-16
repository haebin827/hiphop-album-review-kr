package io.github.haebin827.hiphopreview.kr.dto;

import io.github.haebin827.hiphopreview.kr.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {

    private Integer id;

    private Integer followerId;

    private Integer followingId;

    private LocalDateTime regDate;
}
