package wystudy.jwtsecurity.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import wystudy.jwtsecurity.entity.RefreshToken;
import wystudy.jwtsecurity.repository.TokenRepository;
import wystudy.jwtsecurity.repository.UserRepository;
import wystudy.jwtsecurity.service.CustomUserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    // 키
    private String secretKey = "lalala";

    // 1000L이 1초
    // 어세스 토큰 유효시간 | 20s
    // private long accessTokenValidTime = 20 * 1000L; //
    private long accessTokenValidTime = 30 * 1000L; // 30 * 60 * 1000L;
    // 리프레시 토큰 유효시간 | 1m
    private long refreshTokenValidTime = 60 * 1000L;

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    // 의존성 주입 후, 초기화를 수행
    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Access Token 생성
    public String createAccessToken(String email, List roles){
        return this.createToken(email, accessTokenValidTime, roles);
    }
    // Refresh Token 생성
    public String createRefreshToken(String email, List roles) {
        return this.createToken(email, refreshTokenValidTime, roles);
    }

    // Create token
    public String createToken(String email, long tokenValid, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email); // claims 생성 및 payload 설정
        claims.put("roles", roles);

        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }

    // JWT 에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 회원 정보 추출
    public List<String> getRoles(String token) {
        return userRepository.findById(this.getId(token)).get().getRoles();
    }

    // Request의 Header에서 AccessToken 값을 가져옵니다. "authorization" : "token'
    public String resolveAccessToken(HttpServletRequest request) {
        if(request.getHeader("authorization") != null ) {
            return request.getHeader("authorization").substring(7);
        }
        return null;
    }
    // Request의 Header에서 RefreshToken 값을 가져옵니다. "authorization" : "token'
    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("refreshToken") != null ) {
            return request.getHeader("refreshToken").substring(7);
        }
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            // log.info(e.getMessage());
            //System.out.println(e.getMessage()+"");
            return false;
        }
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    // RefreshToken 존재유무 확인
    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByRefreshToken(refreshToken);
    }

    public boolean isTokenPeriodLeftEnough (String jwtToken) {
        int limitDays = 300000;
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, limitDays);

        date = cal.getTime();

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(date);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public void updateRefreshToken(String originRefreshToken, String newRefreshToken) {
        tokenRepository.deleteByRefreshToken(originRefreshToken);
        tokenRepository.save(new RefreshToken(newRefreshToken));
    }
}