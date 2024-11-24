package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column
    private boolean isImp;

    @Column
    private int views;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modDate;

    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now();
        views = 0;
    }

    public void change(String title, String content, Boolean isImp) {
        this.title = title;
        this.content = content;
        this.isImp = isImp;
        this.modDate = LocalDateTime.now();
    }
}
