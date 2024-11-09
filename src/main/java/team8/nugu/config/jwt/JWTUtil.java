package team8.nugu.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){
        // String 타입의 SecretKey를 암호화하여 객체 타입으로 저장
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 검증을 진행할 3개의 메소드
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public Long getId(String token) {
        return Long.parseLong(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", String.class));
    }

    // 토큰 만료 체크
    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 토큰 생성 메서드
    public String createJwt(String username, Long id, Long expiredMs){
        return Jwts.builder()
                .claim("id", id)
                .claim("username", username)
                .issuedAt(new Date(System.currentTimeMillis())) // 현재 발행 시각
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey) // 암호화
                .compact();
    }

}
