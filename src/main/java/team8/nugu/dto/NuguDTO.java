package team8.nugu.dto;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class NuguDTO {
    private String nickname;
    private String mbti;
    private String org;
    private JsonNullable<String> insta_url = JsonNullable.undefined();
    private String intro;
    private String keyword1;
    private String keyword2;
    private String keyword3;
}
