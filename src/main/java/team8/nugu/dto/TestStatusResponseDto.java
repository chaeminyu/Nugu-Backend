package team8.nugu.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestStatusResponseDto {
    private boolean hasTest;
    private Long testId;
    private int totalParticipants;
}
