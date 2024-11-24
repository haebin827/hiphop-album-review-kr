package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    UserDetails loadUserByUsername(String username);
    void registerUser(UserDTO userDTO);
}
