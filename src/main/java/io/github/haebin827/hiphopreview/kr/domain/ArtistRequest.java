package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="artist_requests")
public class ArtistRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column//(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String type;

    @Column
    private String bornedIn; // XXXX & XXXX-XX-XX

    @Column
    private String country;

    @Column(length = 5000)
    private String tags;

    @Column
    private String s3url;

    @Column
    private String uuid;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;

    @Column(nullable = false)
    private boolean isProcessed;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @PrePersist
    public void onCreate() {
        regDate = LocalDateTime.now();
    }
}