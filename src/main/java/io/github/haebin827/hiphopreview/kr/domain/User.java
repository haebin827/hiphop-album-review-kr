package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length=15, nullable = false, unique = true)
    private String username;

    @Column(length=15, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column
    private String instagramId;

    @Column
    private String email;

    @Column
    private String profilePicture;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    @OneToMany(mappedBy = "following")
    private List<Follow> followers;

    @OneToMany(mappedBy = "user")
    private List<AlbumRequest> albumRequests;

    @ManyToMany(fetch = FetchType.EAGER) // 즉시 로딩
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    // 역할 추가 메서드
    public void addRole(Role role) {
        this.roles.add(role);
    }

    @PrePersist
    protected void onCreate() {
        this.bio = "";
        this.regDate = LocalDateTime.now();
    }
}
