package io.github.haebin827.hiphopreview.kr.repository;
import io.github.haebin827.hiphopreview.kr.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    User findByUsername(String username);

    Boolean existsByUsername(String username);

    User findByEmail(String email);
}
