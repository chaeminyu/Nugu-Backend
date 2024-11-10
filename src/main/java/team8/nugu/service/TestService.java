package team8.nugu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team8.nugu.dto.TestRequestDto;
import team8.nugu.dto.TestStatusResponseDto;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.Users;
import team8.nugu.repository.TestRepository;
import team8.nugu.repository.TestResultRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;

    // 사용자 테스트 생성 상태 확인
    public TestStatusResponseDto checkTestStatus(Users user) {
        TestEntity test = testRepository.findByUser(user);

        TestStatusResponseDto response = new TestStatusResponseDto();
        if (test == null) {
            response.setHasTest(false);
            response.setTestId(null);
        } else {
            response.setHasTest(true);
            response.setTestId(test.getId());
            response.setTotalParticipants((int) testResultRepository.countByTestId(test.getId()));
        }
        return response;
    }

    // 사용자가 테스트 생성 했는지 확인
    public boolean hasCreatedTest(Users user) {
        return testRepository.existsByUser(user);
    }


    // 퀴즈 생성 메서드
    @Transactional
    public Long createTest(TestRequestDto request, Users user) {
        // DTO의 유효성 검사
        if (!request.isValid()) {
            throw new IllegalArgumentException("Invalid test data");
        }

        TestEntity test = new TestEntity();
        test.setUser(user);
        test.setAnswers(request.getAnswers());

        return testRepository.save(test).getId();
    }

    // 퀴즈 조회 메서드
    public TestEntity getTest(Long testId){
        return testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("테스트가 없습니다"));
    }
}
