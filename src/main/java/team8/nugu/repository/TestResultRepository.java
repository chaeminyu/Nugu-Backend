package team8.nugu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team8.nugu.entity.TestResultEntity;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResultEntity, Long> {

    // 특정 테스트에 대한 결과 조회
    List<TestResultEntity> findByTestId(Long testId);

    // 특정 사용자의 테스트 결과 조회
    List<TestResultEntity> findByUserId(Long userId);

    // 특정 퀴즈의 총 참여자 수 조회
    long countByTestId(Long testId);
}
