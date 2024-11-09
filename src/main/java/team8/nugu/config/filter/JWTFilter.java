package team8.nugu.config.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import team8.nugu.config.jwt.JWTUtil;
import team8.nugu.dto.CustomUserDetails;
import team8.nugu.entity.Users;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청(request)에서 Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            // 해당 필터를 종료하고 request,response를 다음 필터로 넘김.
            filterChain.doFilter(request, response);

            // 메소드 종료(필수)
            return;
        }

        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)){
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            // 메소드 종료 (필수)
            return;
        }

        // username과 id 획득
        String username = jwtUtil.getUsername(token);
//        Long id = jwtUtil.getId(token);

        // 사용자 생성
        Users user = new Users();
//        user.setId(id);
        user.setUsername(username);
        user.setPassword("temppassword"); // 비밀번호 임의로 초기화

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자를 (일시적으로) 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}