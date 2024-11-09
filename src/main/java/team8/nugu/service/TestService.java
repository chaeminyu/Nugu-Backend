package team8.nugu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team8.nugu.dto.TestRequestDto;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.Users;
import team8.nugu.repository.TestRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {
    private final TestRepository testRepository;

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
