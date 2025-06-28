package com.babit.demo.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService { //Redis에 RefreshToken 저장/조회/삭제/검증 담당

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    //저장
    public void saveRefreshToken(String key, String refreshToken) {
        redisTemplate.opsForValue().set(key, refreshToken, refreshExpirationTime, TimeUnit.MILLISECONDS);
    }

    //조회
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    //삭제
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    //동일한 RefreshToken인지 확인
    public boolean isSameToken(String userKey, String tokenToCheck) {
        String savedToken = redisTemplate.opsForValue().get(userKey);
        return tokenToCheck.equals(savedToken);
    }

    //블랙리스트 저장
    public void addBlackList(String key, String accessToken, long expirationTime){
        redisTemplate.opsForValue().set(key, accessToken, expirationTime, TimeUnit.MILLISECONDS);
    }

}
