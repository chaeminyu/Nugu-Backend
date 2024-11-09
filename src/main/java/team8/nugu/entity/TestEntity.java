package team8.nugu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team8.nugu.common.converter.StringListConverter;

import java.util.ArrayList;
import java.util.List;
import team8.nugu.entity.Users;

@Entity
@Getter @Setter
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Users user; // 추후에 지윤 언니의 User 클래스 import할 것

    // JPA 엔티티에서 String[] 배열 타입은 직접 사용할 수 없음 -> converter 사용
    @Convert(converter = StringListConverter.class)
    @Column(name = "answer", columnDefinition = "TEXT") // 긴 문자열 저장을 위해 TEXT 타입 사용
    private List<String> answers = new ArrayList<>();

    // 연관관계 편의 메서드 추가- user_id란이 null일 수 없음
    public void setUser(Users user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }
}
