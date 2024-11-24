package io.github.haebin827.hiphopreview.kr.dto;

import io.github.haebin827.hiphopreview.kr.domain.Follow;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "아이디는 최소 3자 이상, 최대 15자 이하여야 합니다.")
    private String username;

    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 최소 3자 이상, 최대 15자 이하여야 합니다.")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 8자리 이상 20자리 이하이며, 최소 하나의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;
    
    @Size(max = 255, message = "자기소개는 최대 255자 이하여야 합니다.")
    private String bio;

    private String instagramId;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    private MultipartFile profilePicture;

    private LocalDateTime regDate;

    private List<ReviewDTO> reviews;

    private List<AlbumRequestDTO> albumRequests;

    private int followerCount;

    private int followingCount;

    // service: user.getFollowers().stream().map(f -> f.getFollower().getNickname()).collect(Collectors.toList())
    private List<String> followerNicknameList;

    private List<String> followingNicknameList;

    //private List<Follow> following;

    //private List<Follow> followers;
}
