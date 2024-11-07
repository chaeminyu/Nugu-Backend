package team8.nugu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team8.nugu.dto.TestResultRequestDto;
import team8.nugu.dto.TestResultResponseDto;
import team8.nugu.entity.Users;
import team8.nugu.service.TestResultService;

import java.util.List;

@RestController
@RequestMapping("/api/test-results") // API 명세서를 기반으로 변경할 것- 임시
@RequiredArgsConstructor
public class TestResultController {
    private final TestResultService testResultService;

    // 퀴즈 답안 제출
    @PostMapping
    public ResponseEntity<TestResultResponseDto> submitTestResult(
            @RequestBody TestResultRequestDto request,
            @AuthenticationPrincipal Users user
    ) {
        TestResultResponseDto response = testResultService.submitTestResult(request, user);
        return ResponseEntity.ok(response);
    }

    // 특정 퀴즈의 결과 목록 조회
    @GetMapping("/quiz/{testId}")
    public ResponseEntity<List<TestResultResponseDto>> getTestResults(
            @PathVariable Long testId
    ){
        List<TestResultResponseDto> results = testResultService.getTestResults(testId);
        return ResponseEntity.ok(results);
    }
}
