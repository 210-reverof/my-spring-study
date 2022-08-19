package mystudy.querydsltest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import mystudy.querydsltest.entity.QTest;
import mystudy.querydsltest.entity.Test;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TestRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public TestRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Test.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Integer> findAllTestByNumber(Integer minNum) {
        QTest t = QTest.test;

        return jpaQueryFactory
                .select(t.testId)
                .from(t)
                .where(t.number.gt(minNum))
                .fetch();
    }
}
