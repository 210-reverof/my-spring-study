package mystudy.querydsltest.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDto {
    private String testStr;

    public TestDto(String testStr) {
        this.testStr = testStr;
    }
}
