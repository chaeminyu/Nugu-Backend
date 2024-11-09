package team8.nugu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    private String mbti;

    private String org;

    private String insta_url;

    private String intro;

    private String keyword1;
    private String keyword2;
    private String keyword3;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid = UUID.randomUUID();

}
