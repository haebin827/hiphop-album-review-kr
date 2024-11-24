package io.github.haebin827.hiphopreview.kr.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
    @Column: JPA에서 엔티티의 필드를 데이터베이스의 컬럼과 매핑할 때 사용

            - name: 필드를 매핑할 데이터베이스 컬럼의 이름을 지정.
                    예시: @Column(name = "user_name")
            - nullable: null 값 허용 여부를 설정. true = null 값 허용, false = 허용하지 X => 기본값은 true
                    예시: @Column(nullable = false)
            - unique: 유니크 제약 조건을 설정 => 기본값은 false
                    예시: @Column(unique = true)
            - length: 문자열 타입(VARCHAR) 컬럼의 최대 길이를 지정 => 기본값은 255
                    예시: @Column(length = 100)
            - precision: 소수점 수를 사용하는 고정 소수점 타입(예: DECIMAL)의 전체 자릿수를 지정 => 기본값은 0 (정수형 타입에는 적용되지 않음)
                    예시: @Column(precision = 10, scale = 2)
            - scale: 고정 소수점 타입에서 소수 이하 자릿수를 지정 => 기본값은 0 (정수형 타입에는 적용되지 않음)
                    예시: @Column(precision = 10, scale = 2)
            - insertable: 삽입 시 이 컬럼을 포함할지 여부를 지정합니다. false로 설정하면 INSERT 문에서 제외됨 => 기본값은 true
                    예시: @Column(insertable = false)
            - updatable: 업데이트 시 이 컬럼을 포함할지 여부를 지정. false로 설정하면 UPDATE 문에서 제외됨 => 기본값은 true
                    예시: @Column(updatable = false)
            - columnDefinition: 컬럼의 데이터베이스 정의(타입, 제약 조건 등)를 직접 지정 가능.
                                데이터베이스에서 특정 타입이나 제약 조건을 명시하고 싶을 때 사용.
                    예시: @Column(columnDefinition = "TEXT")
            - table: 필드를 매핑할 컬럼이 다른 테이블에 있을 경우 해당 테이블의 이름을 지정.
                    예시: @Column(table = "user_details")
            - default: 기본적으로 지원하지 않지만, columnDefinition과 함께 사용할 수 있음. 특정 값이 없을 때 기본값을 지정하는 용도.
                    예시: @Column(columnDefinition = "VARCHAR(255) DEFAULT 'N/A'")
     */
    @Column(nullable = false)
    private String title;

    /*
    Many-to-One: @ManyToOne 어노테이션을 사용

    @ManyToOne: 관계의 type을 지정 (ex. Artist와 Album의 관계에서 여러 Album이 한 Artist에 연결될 수 있음
                    - fetch(FetchType) 속성: 연결된 엔티티를 언제 로딩할지 결정하는 옵션 (모든 관계에서 사용가능)
                    - optional(boolean) 속성: 해당 연관 관계 필드가 null 값을 가질 수 있는지 여부를 지정
                                            - true: 연관된 엔티티가 없어도 관계를 허용 => 기본값
                                            - false: 반드시 연관된 엔티티가 있어야 함
                    - cascade(CascadeType[]) 속성: 연관된 엔티티에 영속성 작업(저장, 삭제 등)을 전파하는 옵션을 설정
                                                    - CascadeType.ALL: 모든 영속성 작업을 연관된 엔티티에 전파
                                                    - CascadeType.PERSIST: 엔티티를 저장할 때 연관된 엔티티도 함께 저장
                                                    - CascadeType.MERGE: 엔티티를 병합할 때 연관된 엔티티도 함께 병합
                                                    - CascadeType.REMOVE: 엔티티를 삭제할 때 연관된 엔티티도 함께 삭제
                                                    - CascadeType.REFRESH: 엔티티를 새로고침할 때 연관된 엔티티도 함께 새로고침
                                                    - CascadeType.DETACH: 엔티티를 분리할 때 연관된 엔티티도 함께 분리
    @JoinColumn: 이 관계를 나타내는 FK의 column을 지정. 관계에서 어떤 컬럼을 사용해 연결할지 명시하며,
                데이터베이스에서는 @JoinColumn에 지정된 컬럼이 외래 키 역할
                    - name 속성: 해당 테이블에서 사용할 외래 키 컬럼의 이름을 정의
                                (ex. Album 테이블에 artist_id라는 컬럼이 생성됨)
                    - referencedColumnName 속성: 연결할 대상 엔티티의 기본 키 컬럼 이름을 지정
                                                (ex. 여기서 Album의 artist_id는 Artist 테이블의 id 필드를 참조)
                    - nullable 속성: 외래 키 컬럼이 null을 허용하는지 여부를 설정. 기본값은 true
                    - unique: 외래 키 컬럼에 고유 제약 조건을 설정

    ⭐ @ManyToOne과 @JoinColumn은 꼭 함께 쓰일 필요는 없음.
        @ManyToOne만으로도 다대일 관계를 정의할 수 있지만, 명확한 외래 키 컬럼을 지정하고 싶을 때 @JoinColumn을 추가로 사용.

        @ManyToOne을 단독으로 사용하는 경우: @ManyToOne만 사용하면 JPA는 기본 네이밍 전략을 따라 자동으로 외래 키 컬럼을 생성.
                                        이 경우, JPA는 기본적으로 연결된 엔티티 이름(artist)에 _id를 추가하여 외래 키 컬럼을 생성. 따라서 Album 테이블에는 artist_id라는 이름의 외래 키 컬럼이 자동으로 생성됨.
                                        @JoinColumn의 referencedColumnName 속성의 기본값은 참조하는 엔티티의 기본 키(primary key) 컬럼

        @JoinColumn을 함께 사용하는 이유:
                외래 키 컬럼의 이름을 명시적으로 설정가능: @JoinColumn을 사용하면 외래 키 컬럼 이름을 artist_id가 아닌 creator_id 등으로 원하는 대로 지정할 수 있음.
                참조할 대상 컬럼을 지정: 기본적으로는 대상 엔티티의 기본 키가 참조되지만, referencedColumnName 속성을 사용해 Artist 엔티티의 특정 필드를 참조하도록 할 수도 있음.
    */

    /*@ManyToMany
    @JoinTable(
            name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "artistUuid"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "uuid")
    )
    @Builder.Default // 필드 초기화가 @Builder에서도 적용되도록 하기 위해 @Builder.Default를 사용 => JUnit Test 결과 출력용
    @ToString.Exclude
    private List<Artist> artists = new ArrayList<>();*/

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "uuid")
    private Artist artist;

    @Column
    private String primaryType;

    @Column
    private String secondaryType;

    @Column
    private String year;

    @Column
    private float rating;

    //@Column
    //private int tracks;

    @Column
    private String link;

    @Column
    private String image;

    @Column
    private String s3url;

    @Column
    private boolean isActive;

    /*
    One-to-Many: @OneToMany 어노테이션을 사용

    @OneToMany: 관계의 type을 지정
                    - mappedBy(String) 속성: 연관 관계의 주인을 지정. 상대 엔티티에서 이 엔티티를 참조하는 필드 이름을 지정.
                                            즉, 상대 엔티티에서 이 엔티티를 참조하는 필드명
                    - fetch(fetchType) 속성: 연결된 엔티티를 언제 로딩할지 결정하는 옵션 (모든 관계에서 사용가능)
                                               - FetchType.LAZY (지연 로딩): 기본값. 필요할 때 데이터베이스에서 조회.
                                               - FetchType.EAGER (즉시 로딩): 엔티티 로딩 시점에 즉시 연관된 데이터를 로딩.
                    - cascade(CascadeType[]) 속성: 연관된 엔티티에 영속성 작업(저장, 삭제 등)을 전파하는 옵션을 설정
                                                    - CascadeType.ALL: 모든 영속성 작업을 연관된 엔티티에 전파
                                                    - CascadeType.PERSIST: 엔티티를 저장할 때 연관된 엔티티도 함께 저장
                                                    - CascadeType.MERGE: 엔티티를 병합할 때 연관된 엔티티도 함께 병합
                                                    - CascadeType.REMOVE: 엔티티를 삭제할 때 연관된 엔티티도 함께 삭제
                                                    - CascadeType.REFRESH: 엔티티를 새로고침할 때 연관된 엔티티도 함께 새로고침
                                                    - CascadeType.DETACH: 엔티티를 분리할 때 연관된 엔티티도 함께 분리
                    - orphanRemoval(boolean) 속성: 부모 엔티티와의 관계가 끊어진 (삭제된) 자식 엔티티를 자동으로 삭제할지 여부를 결정
                                                    - true: 부모 엔티티와의 관계가 끊어지면 자식 엔티티도 자동으로 삭제.
                                                    - false: 기본값으로 관계가 끊어져도 자식 엔티티는 삭제되지 않음.
    */

    @OneToMany(mappedBy = "album", cascade = {CascadeType.MERGE})
    private List<Review> reviews;

    /*
    @PrePersist: Spring Data JPA의 save() 메서드 호출 시 엔티티가 데이터베이스에 저장되기 전에 해당 메서드가 자동으로 호출됨.
                이를 통해 데이터 저장 전, 특정 필드의 초기값을 설정하거나 다른 작업 수행가능.

    @PrePersist의 동작 방식
                - @PrePersist는 JPA의 라이프사이클 콜백 어노테이션 중 하나로, 엔티티가 영속성 컨텍스트에 저장되기 직전에 실행됨.
                - Spring Data JPA의 save() 메서드를 호출하면 엔티티가 영속화되는 시점에 자동으로 @PrePersist 메서드가 실행됨.
    */

    @Column(unique = true)
    private String uuid;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @PrePersist
    protected void onCreate() {
        this.isActive = true;
        this.rating = 0f;
        this.regDate = LocalDateTime.now();
    }
}
