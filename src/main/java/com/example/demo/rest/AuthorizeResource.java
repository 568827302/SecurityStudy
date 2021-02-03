package com.example.demo.rest;

import javax.validation.Valid;

import com.example.demo.config.AppProperties;
import com.example.demo.domain.Auth;
import com.example.demo.domain.dto.LoginDto;
import com.example.demo.domain.dto.UserDto;

import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authorize")
@RestController
@RequiredArgsConstructor
public class AuthorizeResource {
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final AppProperties properties;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
    }

    @PostMapping("/token")
    public Auth login(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/token/refresh")
    public Auth refreshToken(@RequestHeader("Authorization") String header,
                             @RequestParam String refreshToken) {
        String accessToken = header.replace(properties.getJwt().getPrefix(), "");
        if(jwtUtils.validRefreshToken(refreshToken) && jwtUtils.validAccessTokenWithoutExpired(accessToken)) {
            return Auth.builder()
                    .refreshToken(refreshToken)
                    .accessToken(jwtUtils.buildAccessTokenWithRefreshToken(refreshToken))
                    .build();
        }
        throw new AccessDeniedException("Bad Credentials");
    }
}
