package team8.nugu.dto;

import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class UserDTO {

    @NotNull // null 값으로 넘어올 경우 NullPointException
    private String username;
    @NotNull
    private String password;
    private String password2;
    private String nickname;
    private String mbti;
    private String org;
    private String insta_url;
    private String intro;
    private String keyword1;
    private String keyword2;
    private String keyword3;

    @Builder
    public UserDTO(String username, String password, String nickname,
                   String mbti, String org, String insta_url, String intro,
                   String keyword1, String keyword2, String keyword3) {
        this.username = username;
        this.password = password;
        this.password2 = password;
        this.nickname = nickname;
        this.mbti = mbti;
        this.org = org;
        this.insta_url = insta_url;
        this.intro = intro;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
    }

}
