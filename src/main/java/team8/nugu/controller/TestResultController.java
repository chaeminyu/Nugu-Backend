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
@RequestMapping("/test-results")
@RequiredArgsConstructor
public class TestResultController {
    private final TestResultService testResultService;

    // UUID로 테스트 응시 및 결과 제출
    @PostMapping("/submit/{uuid}")
    public ResponseEntity<TestResultResponseDto> submitTestResult(
            @PathVariable String uuid,
            @RequestBody TestResultRequestDto request
    ) {
        try {
            TestResultResponseDto response = testResultService.submitTestResult(uuid, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 특정 테스트의 순위표 조회
    @GetMapping("/{uuid}/rankings")
    public ResponseEntity<List<TestResultResponseDto>> getTestResults(
            @PathVariable String uuid
    ) {
        try {
            List<TestResultResponseDto> results = testResultService.getTestResultsByUuid(uuid);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}