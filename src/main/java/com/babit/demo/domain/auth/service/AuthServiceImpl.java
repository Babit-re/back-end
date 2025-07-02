package com.babit.demo.domain.auth.service;
import com.babit.demo.domain.auth.service.RefreshTokenService;
import com.babit.demo.domain.auth.dto.LoginRequestDto;
import com.babit.demo.domain.auth.dto.TokenDto;
import com.babit.demo.domain.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Log4j2
public class AuthServiceImpl implements AuthService {  //토큰을 발급할지 말지 결정하는 인증 흐름 담당

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Transactional
    @Override
    public TokenDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenService.saveRefreshToken(authentication.getName(), refreshToken); // Redis 저장

        return new TokenDto(accessToken, refreshToken);
    }

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void logout(String accessToken, String email){
        // 로그아웃 하고 싶은 토큰이 유효한 지 먼저 검증하기
        jwtTokenProvider.validateToken(accessToken);

        // Access Token에서 User email 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // Redis에서 해당 User email로 저장된 Refresh Token 이 있는지 여부를 확인 후에 있을 경우 삭제
        if (refreshTokenService.getRefreshToken(authentication.getName())!=null){
            // Refresh Token을 삭제
            refreshTokenService.deleteRefreshToken(authentication.getName());
        }

        //access token을 BlackList에 저장하기
        Long expiration = jwtTokenProvider.getExpiration(accessToken); // 해당 Access Token 유효시간을 가지고 옴
        refreshTokenService.addBlackList(accessToken,"logout",expiration); //key 값은 Access Token이고 value 값은 아무거나 들어가면 됨(ex. "logout")
    }

    @Override
    public TokenDto reissueToken(String refreshTokenToCheck) {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshTokenToCheck);

        log.info("refresh token 검증완료");

        // Access Token 에서 UserEmail을 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenToCheck);
        log.info("refreshToken으로부터 얻은 이메일: {}", authentication.getName());

        // Redis에서 저장된 Refresh Token 값을 가져와서 비교
        if (!refreshTokenService.isSameToken(authentication.getName(), refreshTokenToCheck)) {
            log.info("stored token = {}", refreshTokenService.getRefreshToken(authentication.getName()));
            log.info("incoming token = {}", refreshTokenToCheck);
            throw new AuthenticationException("Refresh Token이 유효하지 않습니다.") {};
        }

        // 토큰 재발행
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenService.saveRefreshToken(authentication.getName(), newRefreshToken); // Redis 저장

        return new TokenDto(newAccessToken, newRefreshToken);
    }
}
