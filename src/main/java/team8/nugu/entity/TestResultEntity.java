package team8.nugu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team8.nugu.common.converter.StringListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TestResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private TestEntity test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Convert(converter = StringListConverter.class)
    @Column(name = "answer", columnDefinition = "TEXT")
    private List<String> answers = new ArrayList<>();

    @Column(name = "nickname")
    private String nickname;
}
