package team8.nugu.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team8.nugu.config.jwt.JWTUtil;
import team8.nugu.dto.CustomUserDetails;
import team8.nugu.dto.NuguDTO;
import team8.nugu.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<NuguDTO> getUser(HttpServletRequest request) {
        // accessToken 가져오기
        String Token = request.getHeader("Authorization");
        if(Token == null || Token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = Token.substring(7);

        NuguDTO nugu = userService.getInfo(jwtUtil.getUsername(accessToken));
        return ResponseEntity.status(HttpStatus.OK)
                .body(nugu);
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(HttpServletRequest request, @RequestBody NuguDTO nugu) {
        // accessToken 가져오기
        String Token = request.getHeader("Authorization");
        if(Token == null || Token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The token is invalid.");
        }

        String accessToken = Token.substring(7);
        userService.updateInfo(jwtUtil.getUsername(accessToken), nugu);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/uuid")
    public ResponseEntity<String> getUUID() {
        // 로그인한 사용자의 username 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("유저 이름 : " + username);

        // 로그인한 사용자의 uuid 반환
        String uuid = userService.getUUID(username);
        return ResponseEntity.status(HttpStatus.OK).body(uuid);
    }

}
