package com.example.demo.service;

import com.example.demo.domain.Auth;
import com.example.demo.repository.UserRepo;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public Auth login(String username ,String password) throws AuthenticationException {
        return userRepo.findOptionalByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> Auth.builder()
                        .signToken(jwtUtils.createAccessJwtToken(user))
                        .refreshToken(jwtUtils.createRefreshJwtToken(user))
                        .build()
                ).orElseThrow(() -> new BadCredentialsException("账户密码认证失败"));
    }
}
