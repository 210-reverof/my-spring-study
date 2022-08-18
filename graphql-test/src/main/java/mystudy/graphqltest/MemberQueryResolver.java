package mystudy.graphqltest;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class MemberQueryResolver implements GraphQLQueryResolver {
    private final MemberRepository memberRepository;

    public Member findMemberById(Integer id) {
        if (id <= 0)  return null;

        return this.memberRepository
                .findById(id)
                .orElse(null);
    }

    public List<Member> findAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members;
    }
}