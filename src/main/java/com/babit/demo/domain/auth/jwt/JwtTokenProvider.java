package com.babit.demo.domain.auth.jwt;

import com.babit.demo.domain.auth.service.RefreshTokenService;
import com.babit.demo.domain.auth.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {  //JwtTokenProvider는 토큰 생성/파싱/검증만 담당

    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

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
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("만료된 토큰입니다.") {};
        } catch (JwtException e) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.") {};
        }
    }

}
