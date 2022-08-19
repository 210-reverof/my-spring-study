package mystudy.querydsltest.service;

import lombok.RequiredArgsConstructor;
import mystudy.querydsltest.dto.TestDto;
import mystudy.querydsltest.repository.TestRepository;
import mystudy.querydsltest.repository.TestRepositorySupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Component
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final TestRepositorySupport testRepositorySupport;

    public TestDto testServiceMethod(String str) {
        TestDto testDto = new TestDto(str);

        return testDto;
    }

    public List<Integer> testSupportRepo(Integer minNum) {
        List<Integer> tests = testRepositorySupport.findAllTestByNumber(minNum);

        return tests;
    }
}