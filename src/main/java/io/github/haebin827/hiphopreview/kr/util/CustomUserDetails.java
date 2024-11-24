package io.github.haebin827.hiphopreview.kr.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final String name;       // 추가 정보: 이름
    private final String nickname;   // 추가 정보: 닉네임

    public CustomUserDetails(String username, String password, String name, String nickname,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.name = name;
        this.nickname = nickname;
    }

}
