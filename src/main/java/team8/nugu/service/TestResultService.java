package team8.nugu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team8.nugu.dto.TestResultRequestDto;
import team8.nugu.dto.TestResultResponseDto;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.TestResultEntity;
import team8.nugu.entity.Users;
import team8.nugu.repository.TestRepository;
import team8.nugu.repository.TestResultRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestResultService {
    private final TestResultRepository testResultRepository;
    private final TestRepository testRepository;

    // 테스트 결과 제출 처리 메서드
    @Transactional
    public TestResultResponseDto submitTestResult(String uuid, TestResultRequestDto request) {
        try {
            //1. UUID 유효성 검사
            if (uuid == null || uuid.trim().isEmpty()) {
                throw new IllegalArgumentException("UUID는 null값일 수 없습니다");
            }
            //2. request 유효성 검사
            if (request == null) {
                throw new IllegalArgumentException("request는 null값일 수 없습니다.");
            }
            if(!request.isValid()) {
                throw new IllegalArgumentException("테스트 결과가 유효하지 않습니다: 닉네임과 10문항에 대한 답변은 필수 항목입니다.");
            }

            // 1. UUID로 테스트 조회
            TestEntity test = testRepository.findByUserUuid(uuid);
            if (test == null) {
                throw new IllegalArgumentException("해당 테스트는 존재하지 않습니다.");
            }
            // 2. 요청 데이터 유효성 검사
            if (!request.isValid()) {
                throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다.");
            }
            // 3. 답안 채점
            int correctCount = calculateCorrectAnswers(test.getAnswers(), request.getUserAnswers());
            // 4. 결과 저장
            TestResultEntity result = new TestResultEntity();
            result.setTest(test);
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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID 포맷이 정확하지 않거나 테스트를 찾을 수 없습니다: " + uuid);
        }
    }

    // UUID로 테스트 결과 목록(=순위표) 조회
    public List<TestResultResponseDto> getTestResultsByUuid(String uuid) {
        // 1. UUID로 테스트 조회
        TestEntity test = testRepository.findByUserUuid(uuid);
        if (test == null) {
            throw new IllegalArgumentException("테스트가 존재하지 않습니다.");
        }

        // 2. 해당 테스트의 모든 결과 조회
        List<TestResultEntity> results = testResultRepository.findByTestId(test.getId());

        // 3. 각 결과를 DTO로 변환
        return results.stream()
                .map(result -> {
                    int correctCount = calculateCorrectAnswers(
                            test.getAnswers(),
                            result.getAnswers()
                    );
                    int rank = calculateRank(test.getId(), correctCount);
                    return TestResultResponseDto.builder()
                            .nickname(result.getNickname())
                            .correctAnswers(correctCount)
                            .rank(rank)
                            .totalParticipants(results.size())
                            .build();
                })
                .collect(Collectors.toList());
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
}
