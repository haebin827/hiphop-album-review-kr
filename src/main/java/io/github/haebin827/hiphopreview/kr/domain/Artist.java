package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name="artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String gender;

    @Column
    private String type;

    @Column
    private String bornedIn; // XXXX & XXXX-XX-XX

    @Column
    private String country;

    @Column(length = 5000)
    private String tags;

    @Column
    private String image;

    @Column
    private String s3url;

    @Column(unique = true)
    private String uuid;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Album> albums = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        regDate = LocalDateTime.now();
    }

}