package wystudy.jwtsecurity.dto;

import lombok.Getter;

@Getter
public class SignUpDTO {
    // 검증 조건 추가하기
    private String id;
    private String password;
    private String username;
}
