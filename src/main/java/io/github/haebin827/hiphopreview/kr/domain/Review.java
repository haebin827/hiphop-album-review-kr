package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int dislikes;

    @CreatedDate
    @Column(name = "regDate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
        this.likes = 0;
        this.dislikes = 0;
        regDate = LocalDateTime.now();
    }

}