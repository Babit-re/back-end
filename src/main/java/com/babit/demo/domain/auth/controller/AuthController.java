package com.babit.demo.domain.auth.controller;

import com.babit.demo.domain.auth.dto.LoginRequestDto;
import com.babit.demo.domain.auth.dto.LogoutRequestDto;
import com.babit.demo.domain.auth.dto.TokenDto;
import com.babit.demo.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        //로그인 시도
        TokenDto tokenDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout")
    public void logout(
            @RequestBody LogoutRequestDto request){
        authService.logout(request.getAccessToken(), request.getEmail());
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        TokenDto tokenDto = authService.reissueToken(refreshToken);
        return ResponseEntity.ok(tokenDto);
    }
}