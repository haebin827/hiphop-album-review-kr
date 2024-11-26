package io.github.haebin827.hiphopreview.kr.service;

import io.github.haebin827.hiphopreview.kr.domain.Role;
import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.dto.UserDTO;
import io.github.haebin827.hiphopreview.kr.repository.RoleRepository;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import io.github.haebin827.hiphopreview.kr.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService, UserDetailsService  {

    private final UserRepository ur;
    private final RoleRepository rr;
    private final ModelMapper mm;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = ur.findByUsername(username); // 데이터베이스에서 사용자 조회
        log.info("USER: " + user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found"); // 사용자가 존재하지 않을 경우 예외 발생
        }

        // 사용자 권한을 로드
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // CustomUserDetails 객체 반환
        CustomUserDetails customUserDetails = new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getNickname(),
                authorities
        );
        log.info("CustomUserDetails: {}", customUserDetails); // 디버깅: 반환된 객체 확인
        return customUserDetails;
    }

    public void registerUser(UserDTO userDTO) {
        // 비밀번호 암호화
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // UserDTO를 User 엔터티로 변환
        User user = mm.map(userDTO, User.class);
        // 기본 역할 설정
        Role userRole = rr.findByName("ROLE_USER");
        if (userRole == null) {
            throw new RuntimeException("Default role ROLE_USER not found in database");
        }
        user.addRole(userRole);
        // User 저장
        ur.save(user);
    }

}
