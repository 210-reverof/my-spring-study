package wystudy.jwtsecurity.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wystudy.jwtsecurity.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        // 유효한 토큰인지 확인합니다.
        if (accessToken != null) {
            // 어세스 토큰이 유효한 상황
            if (jwtTokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
                request.setAttribute("user_id", jwtTokenProvider.getId(accessToken));
            }
            else if (!jwtTokenProvider.validateToken(accessToken) && refreshToken == null){
                request.setAttribute("exception", "access token end" );
            }
            // 어세스 토큰이 만료된 상황 | 리프레시 토큰 또한 존재하는 상황
            else if (!jwtTokenProvider.validateToken(accessToken) && refreshToken != null) {
                // 재발급 후, 컨텍스트에 다시 넣기
                /// 리프레시 토큰 검증
                boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
                System.out.println(validateRefreshToken);
                /// 리프레시 토큰 저장소 존재유무 확인
                boolean isRefreshToken = jwtTokenProvider.existsRefreshToken(refreshToken);
                System.out.println(isRefreshToken);
                if (validateRefreshToken && isRefreshToken) {
                    if (jwtTokenProvider.isTokenPeriodLeftEnough(refreshToken)) {
                    /// 리프레시 토큰으로 이메일 정보 가져오기
                    String id = jwtTokenProvider.getId(refreshToken);
                    List roles = jwtTokenProvider.getRoles(refreshToken);
                    /// 토큰 발급
                    String newAccessToken = jwtTokenProvider.createAccessToken(id, roles);
                    /// 헤더에 어세스 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                    /// 컨텍스트에 넣기
                    this.setAuthentication(newAccessToken);
                    request.setAttribute("user_id", jwtTokenProvider.getId(newAccessToken));
                        }

                        // 안 남았다면
                        else {
                        /// 리프레시 토큰으로 이메일 정보 가져오기
                        String id = jwtTokenProvider.getId(refreshToken);
                        List roles = jwtTokenProvider.getRoles(refreshToken);
                        /// 토큰 발급
                        String newAccessToken = jwtTokenProvider.createAccessToken(id, roles);
                        String newRefreshToken = jwtTokenProvider.createRefreshToken(id, roles);

                        // 갱신
                        jwtTokenProvider.updateRefreshToken(refreshToken, newRefreshToken);

                        /// 헤더에 어세스 토큰 추가
                        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                        jwtTokenProvider.setHeaderRefreshToken(response, newRefreshToken);

                        /// 컨텍스트에 넣기
                        this.setAuthentication(newAccessToken);

                        request.setAttribute("user_id", jwtTokenProvider.getId(newAccessToken));
                        }

                }
                else {
                    request.setAttribute("exception", "refresh token not available" );
                }
            }
            else {
                request.setAttribute("exception", "refresh token not available" );
            }
        }
        filterChain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
