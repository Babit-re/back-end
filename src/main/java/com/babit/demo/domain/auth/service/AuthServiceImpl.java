package com.babit.demo.domain.auth.service;
import com.babit.demo.domain.auth.service.RefreshTokenService;
import com.babit.demo.domain.auth.dto.LoginRequestDto;
import com.babit.demo.domain.auth.dto.TokenDto;
import com.babit.demo.domain.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Log4j2
public class AuthServiceImpl implements AuthService {  //토큰을 발급할지 말지 결정하는 인증 흐름 담당

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

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
    public TokenDto reissueToken(String refreshTokenToCheck) {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshTokenToCheck);

        // Access Token 에서 UserEmail을 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenToCheck);

        // Redis에서 저장된 Refresh Token 값을 가져와서 비교
        if (!refreshTokenService.isSameToken(authentication.getName(), refreshTokenToCheck)) {
            throw new AuthenticationException("Refresh Token이 유효하지 않습니다.") {};
        }

        // 토큰 재발행
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenService.saveRefreshToken(authentication.getName(), newRefreshToken); // Redis 저장

        return new TokenDto(newAccessToken, newRefreshToken);
    }
}
