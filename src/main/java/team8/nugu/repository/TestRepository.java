package team8.nugu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team8.nugu.entity.TestEntity;
import team8.nugu.entity.Users;

import java.util.UUID;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
    TestEntity findByUser(Users user);
    boolean existsByUser(Users user);
    // User의 UUID로 테스트를 찾는 메서드
    @Query(value = "SELECT t.* FROM test_entity t " +
            "JOIN users u ON t.user_id = u.id " +
            "WHERE u.uuid = UNHEX(REPLACE(:uuid, '-', ''))",
            nativeQuery = true)
    TestEntity findByUserUuid(@Param("uuid") String uuid);
}
