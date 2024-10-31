package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 상속받는 클래스들이 필드를 상속받을 수 있게 설정
@EntityListeners(value = {AuditingEntityListener.class}) // 엔티티의 생명주기 이벤트를 감지하는 리스너 지정
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regDate", updatable = false)
    private LocalDateTime regDate;

    /*@LastModifiedDate
    @Column(name = "modDate")
    private LocalDateTime modDate;*/
}
