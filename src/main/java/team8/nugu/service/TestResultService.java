package team8.nugu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team8.nugu.dto.TestResultRequestDto;
import team8.nugu.dto.TestResultResponseDto;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.TestResultEntity;
import team8.nugu.entity.Users;
import team8.nugu.repository.TestResultRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestResultService {
    private final TestResultRepository testResultRepository;
    private final TestService testService;

    // 퀴즈 결과 제출 처리 메서드
    @Transactional
    public TestResultResponseDto submitTestResult(TestResultRequestDto request, Users user) {
        // 1. 요청 데이터 유효성 검사
        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid test result data");
        }

        // 2. 퀴즈 정보 조회
        TestEntity test = testService.getTest(request.getTestId());

        // 3. 답안 채점
        int correctCount = calculateCorrectAnswers(test.getAnswers(), request.getUserAnswers());

        // 4. 결과 저장
        TestResultEntity result = new TestResultEntity();
        result.setTest(test);
        result.setUser(user);
        result.setAnswers(request.getUserAnswers());
        result.setNickname(request.getNickname());
        testResultRepository.save(result);

        // 5. 등수 계산
        int rank = calculateRank(test.getId(), correctCount);
        int totalParticipants = (int) testResultRepository.countByTestId(test.getId());

        // 6. 응답 생성
        return TestResultResponseDto.builder()
                .nickname(request.getNickname())
                .correctAnswers(correctCount) // 10점 만점 중 몇 점
                .rank(rank) // 등수
                .totalParticipants(totalParticipants) // 총 참여자 수
                .build();
    }

    // 정답 개수 계산 메서드
    private int calculateCorrectAnswers(List<String> correctAnswers, List<String> userAnswers) {
        int count = 0;
        for (int i=0; i<correctAnswers.size(); i++) {
            if (correctAnswers.get(i).equals(userAnswers.get(i))) {
                count ++;
            }
        }
        return count;
    }

    // 등수 계산 메서드
    private int calculateRank(Long testId, int correctCount) {
        List<Integer> scores = testResultRepository.findByTestId(testId).stream()
                .map(result -> calculateCorrectAnswers(result.getTest().getAnswers(), result.getAnswers()))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return scores.indexOf(correctCount) + 1;
    }

    // 특정 퀴즈의 모든 결과를 조회하는 메서드
    public List<TestResultResponseDto> getTestResults(Long testId) {
        // 해당 퀴즈의 모든 결과를 조회
        List<TestResultEntity> results = testResultRepository.findByTestId(testId);

        // 결과에 대해 DTO 반환
        return results.stream()
                .map(result -> {
                    // 1. 각 결과의 점수 계산
                    int correctCount = calculateCorrectAnswers(
                            result.getTest().getAnswers(),
                            result.getAnswers()
                    );

                    // 2. 해당 점수의 등수 계산
                    int rank = calculateRank(testId, correctCount);

                    // 3. 총 참여자 수 조회
                    int totalParticipants = (int) testResultRepository.countByTestId(testId);

                    // 4. DTO 생성 및 반환
                    return TestResultResponseDto.builder()
                            .nickname(result.getNickname())
                            .correctAnswers(correctCount)
                            .rank(rank)
                            .totalParticipants(totalParticipants)
                            .build();
                }).collect(Collectors.toList());
    }
}
