package io.github.haebin827.hiphopreview.kr.repository;
import io.github.haebin827.hiphopreview.kr.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
