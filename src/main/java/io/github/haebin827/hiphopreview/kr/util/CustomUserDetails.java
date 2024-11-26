package io.github.haebin827.hiphopreview.kr.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final Integer id;
    private final String name;
    private final String nickname;

    public CustomUserDetails(Integer id, String username, String password, String name, String nickname,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
        this.nickname = nickname;
    }

}
