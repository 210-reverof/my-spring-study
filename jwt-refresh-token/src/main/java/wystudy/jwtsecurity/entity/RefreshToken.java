package wystudy.jwtsecurity.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 3
public class RefreshToken {
    @Id
    @Column(nullable = false)
    private String refreshToken;

    public RefreshToken(String refreshToken) {
         this.refreshToken = refreshToken;
    }
}
