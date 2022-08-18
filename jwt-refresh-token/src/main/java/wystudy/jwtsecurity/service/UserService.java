package wystudy.jwtsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wystudy.jwtsecurity.dto.SignInDTO;
import wystudy.jwtsecurity.dto.SignUpDTO;
import wystudy.jwtsecurity.entity.User;
import wystudy.jwtsecurity.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(SignUpDTO user) {
        Long userId = userRepository.save(
                        User.builder()
                                .id(user.getId())
                                .password(passwordEncoder.encode(user.getPassword()))
                                .username(user.getUsername())
                                .roles(Collections.singletonList("ROLE_USER"))
                                .build())
                .getUid();
        return userId;
    }

    public User findUser(SignInDTO user) {
        User member = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 잘못되었습니다."));
        return member;
    }

    public boolean checkPassword(User member, SignInDTO user) {
        return passwordEncoder.matches(user.getPassword(), member.getPassword());
    }
}
