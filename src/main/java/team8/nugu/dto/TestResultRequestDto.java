package team8.nugu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestResultRequestDto {
//    private Long testId;
    private List<String> userAnswers;
    private String nickname;

    // 유효성 검사
    public boolean isValid() {
        return userAnswers != null
                // 10문항 모두 답한건지
                && userAnswers.size() == 10
                && nickname != null
                && !nickname.trim().isEmpty(); // 닉네임이 비어있진 않은지 확인
    }
}
