package com.babit.demo.domain.auth.service;

import com.babit.demo.domain.auth.jwt.CustomUserDetails;
import com.babit.demo.domain.user.entity.User;
import com.babit.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { //Spring Security의 UserDetailsService 인터페이스 구현

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // 실제로는 email로 로그인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("유효하지 않은 사용자 이메일: {}", email);
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        return new CustomUserDetails(
                user.getId(), // ← 이거 덕분에 getId() 가능
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }

}