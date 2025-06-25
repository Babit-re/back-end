package com.babit.demo.domain.auth.service;

import com.babit.demo.domain.auth.dto.TokenDto;
import com.babit.demo.domain.auth.dto.LoginRequestDto;

public interface AuthService {

    public TokenDto login(LoginRequestDto loginRequestDto);
    public TokenDto reissueToken(String refreshToken);
}