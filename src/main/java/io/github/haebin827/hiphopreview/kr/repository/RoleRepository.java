package io.github.haebin827.hiphopreview.kr.repository;

import io.github.haebin827.hiphopreview.kr.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
