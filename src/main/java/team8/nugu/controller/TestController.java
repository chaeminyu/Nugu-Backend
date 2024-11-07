package team8.nugu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team8.nugu.dto.TestRequestDto;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.Users;
import team8.nugu.service.TestService;

@RestController
@RequestMapping("/api/tests") // API 명세서에 기반하여 변경할 예정!
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    // 퀴즈 생성
    @PostMapping
    public ResponseEntity<Long> createTest(
            @RequestBody TestRequestDto request,
            @AuthenticationPrincipal Users user
    ) {
        Long testId = testService.createTest(request, user);
        return ResponseEntity.ok(testId);
    }

    // 퀴즈 조회
    @GetMapping("/{testId}")
    public ResponseEntity<TestEntity> getTest(@PathVariable Long testId){
        TestEntity test = testService.getTest(testId);
        return ResponseEntity.ok(test);
    }
}
