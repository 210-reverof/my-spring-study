package mystudy.querydsltest.service;

import lombok.RequiredArgsConstructor;
import mystudy.querydsltest.TestDto;
import mystudy.querydsltest.repository.TestRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public TestDto testServiceMethod(String str) {
        TestDto testDto = new TestDto(str);

        return testDto;
    }
}