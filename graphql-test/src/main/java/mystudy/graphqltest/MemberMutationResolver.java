package mystudy.graphqltest;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(rollbackFor = Exception.class)
public class MemberMutationResolver implements GraphQLMutationResolver {
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        return this.memberRepository.save(member);
    }

    public boolean deleteMemberById(Integer id) {
        if (id <= 0)  return false;

        Member member = this.memberRepository.findById(id).orElse(null);
        if (member == null) return false;

        this.memberRepository.deleteById(id);
        return true;
    }
}
