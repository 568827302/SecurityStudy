package com.example.demo.rest;

import javax.validation.Valid;

import com.example.demo.config.AppProperties;
import com.example.demo.domain.Auth;
import com.example.demo.domain.dto.LoginDto;
import com.example.demo.domain.dto.UserDto;

import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authorize")
@RestController
@RequiredArgsConstructor
public class AuthorizeResource {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
    }

    @PostMapping("/token")
    public Auth login(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/token/refresh")
    public Auth refreshToken(@RequestHeader("Authoritication") String header,
                             @RequestParam String refreshToken) {
        // TODO..
        return null;
    }
}
