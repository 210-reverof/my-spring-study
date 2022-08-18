package mystudy.graphqltest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member save(Member member);
    List<Member> findAll();
}