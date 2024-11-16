package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(unique = true)
    private String instagramId;

    @Column
    private String profilePicture;

    @CreatedDate
    @Column(name = "regDate", updatable = false)
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    @OneToMany(mappedBy = "following")
    private List<Follow> followers;

    @OneToMany(mappedBy = "user")
    private List<AlbumRequest> albumRequests;

    @PrePersist
    protected void onCreate() {
        this.bio = "";
        regDate = LocalDateTime.now();
    }

}
