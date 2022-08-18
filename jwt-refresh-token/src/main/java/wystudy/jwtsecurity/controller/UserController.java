// https://sol-devlog.tistory.com/20?category=1005914
package wystudy.jwtsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wystudy.jwtsecurity.config.JwtTokenProvider;
import wystudy.jwtsecurity.dto.SignInDTO;
import wystudy.jwtsecurity.entity.RefreshToken;
import wystudy.jwtsecurity.entity.User;
import wystudy.jwtsecurity.repository.TokenRepository;
import wystudy.jwtsecurity.service.UserService;
import wystudy.jwtsecurity.dto.SignUpDTO;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody SignUpDTO user) {
        Long result = userService.join(user);
        return result != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다!") :
                ResponseEntity.badRequest().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignInDTO user, HttpServletResponse response) {
        // 유저 존재 확인
        User member = userService.findUser(user);
        // 비밀번호 체크
        userService.checkPassword(member, user);

        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getRoles());
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        // 리프레시 토큰 저장소에 저장
        tokenRepository.save(new RefreshToken(refreshToken));
        return ResponseEntity.ok().body("로그인 성공!");

    }

    // JWT 인증 요청 테스트
    @GetMapping("/test")
    public ResponseEntity test(@RequestAttribute String user_id) {
        return ResponseEntity.ok().body("토큰 인증 완료 및 데이터 불러오기 성공 "+user_id+"님");
    }

    // 통합 예외 핸들러
    @ExceptionHandler
    public String exceptionHandler(Exception exception) {
        return exception.getMessage();
    }
}
