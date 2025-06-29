package com.babit.demo.domain.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;

    // 인증된 사용자의 권한 정보가 저장될 필드
    private final Collection<? extends GrantedAuthority> authorities;

    @Override public String getUsername() { return email; }  // 인증된 사용자의 이메일을 반환
    @Override public String getPassword() { return password; }  // 인증된 사용자의 비밀번호를 반환
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; } // 인증된 사용자의 권한 정보를 반환
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
