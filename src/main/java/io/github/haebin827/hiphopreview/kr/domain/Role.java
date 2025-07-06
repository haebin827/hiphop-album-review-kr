package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ---------------------------------
    // name:
    //
    // ROLE_GUEST
    // ROLE_USER
    // ROLE_ADMIN
    // ---------------------------------
    @Column(nullable = false, unique = true)
    private String name;
}
