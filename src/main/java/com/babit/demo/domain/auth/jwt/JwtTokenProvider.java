package com.babit.demo.domain.auth.jwt;

import com.babit.demo.domain.auth.service.RefreshTokenService;
import com.babit.demo.domain.auth.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {  //JwtTokenProvider는 토큰 생성/파싱/검증만 담당

    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @PostConstruct  //@PostConstruct 메서드로 secretKey를 한번 Base64 인코딩해주는 방식으로 처리
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    //accessToken 생성
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication.getName(), accessExpirationTime);
    }

    //refreshToken 생성
    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication.getName(), refreshExpirationTime);
    }

    //토큰 생성 메서드
    private String createToken(String subject, long expirationMs) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //JWT 토큰을 복호화해서 사용자 정보를 파싱하고, 이를 기반으로 Authentication 객체를 생성
    public Authentication getAuthentication(String token) {
        try{
            String userPrincipal = Jwts.parser().
                    setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }catch(ExpiredJwtException e){
            throw new AuthenticationException("만료된 토큰입니다.") {};
        }catch(JwtException e){
            throw new AuthenticationException("유효하지 않은 토큰입니다."){};
        }
    }

    //http 헤더로부터 bearer 토큰을 가져옴.
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //Access 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Token을 전달받아 Claim의 유효기간을 체크하고 boolean 타입 값을 리턴
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); //토큰의 만료 시간이 지금보다 이전인가?를 확인
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("만료된 토큰입니다.") {};
        } catch (JwtException e) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.") {};
        }
    }

    //Access 토큰 유효시간 가지고 오기
    public long getExpiration(String token){
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Date expiration = claims.getBody().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    //로그아웃된 토큰인지 확인
    public void validateBlackListToken(String token){
        String blackList = refreshTokenService.getRefreshToken(token);
        if(StringUtils.hasText(blackList)){
            throw new AuthenticationException("로그아웃된 사용자 입니다"){};
        }
    }

}
