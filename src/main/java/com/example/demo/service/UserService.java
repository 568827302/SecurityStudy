package com.example.demo.service;

import com.example.demo.domain.Auth;
import com.example.demo.domain.User;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public boolean isUsernameExisted(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    public boolean isEmailExisted(String email) {
        return userRepo.countByEmail(email) > 0;
    }

    public boolean isMobileExisted(String mobile) {
        return userRepo.countByMobile(mobile) > 0;
    }

    public Auth login(String username ,String password) throws AuthenticationException {
        return userRepo.findOptionalByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> Auth.builder()
                        .accessToken(jwtUtils.createAccessToken(user))
                        .refreshToken(jwtUtils.createRefreshToken(user))
                        .build()
                ).orElseThrow(() -> new BadCredentialsException("账户密码认证失败"));
    }

    @Transactional
    public User register(User user) {
        return roleRepo.findOptionalByAuthority("ROLE_USER")
                .map(role -> {
                    val userToSave = user
                            .withAuthorities(Set.of(role))
                            .withPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepo.save(userToSave);
                }).orElseThrow();
    }
}
