package io.github.haebin827.hiphopreview.kr.service;
import io.github.haebin827.hiphopreview.kr.domain.User;
import io.github.haebin827.hiphopreview.kr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository ur;

    public String findByEmail(String email) {
        User user = ur.findByEmail(email);
        return user != null ? user.getEmail() : "";
    }

}
