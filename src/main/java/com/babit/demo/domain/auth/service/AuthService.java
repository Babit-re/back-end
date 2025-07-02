package com.babit.demo.domain.auth.service;

import com.babit.demo.domain.auth.dto.TokenDto;
import com.babit.demo.domain.auth.dto.LoginRequestDto;

public interface AuthService {

    public TokenDto login(LoginRequestDto loginRequestDto);
    public void logout(String accessToken, String userId);
    public TokenDto reissueToken(String refreshToken);  //Access Token 만료 시 새로운 Access Token과 Refresh Token 재발급
}