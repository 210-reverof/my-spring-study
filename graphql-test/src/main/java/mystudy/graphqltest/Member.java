package mystudy.graphqltest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor      // 추가하기! 없으면 graphql query 사용 시에 오류
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, length = 191)
    private String name;

    @Column(nullable = false)
    private Boolean isStudent;

    @Builder
    public Member(String name, Boolean isStudent) {
        this.name = name;
        this.isStudent = isStudent;
    }
}
