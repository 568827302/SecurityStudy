package com.example.demo.rest;

import com.example.demo.config.AppProperties;
import com.example.demo.domain.Auth;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.LoginDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.exception.DuplicateProblem;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RequestMapping("/authorize")
@RestController
@RequiredArgsConstructor
public class AuthorizeResource {
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final AppProperties properties;

    private MessageSource messageSource;

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDto userDto, Locale locale) {
        if (userService.isUsernameExisted(userDto.getUsername())) {
            throw new DuplicateProblem("Exception.duplicate.username", messageSource, locale);
        }
        if (userService.isEmailExisted(userDto.getEmail())) {
            throw new DuplicateProblem("Exception.duplicate.email", messageSource, locale);
        }
        if (userService.isMobileExisted(userDto.getPassword())) {
            throw new DuplicateProblem("Exception.duplicate.mobile", messageSource, locale);
        }

        val user = User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobile(userDto.getMobile())
                .password(userDto.getPassword())
                .build();

        userService.register(user);
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
