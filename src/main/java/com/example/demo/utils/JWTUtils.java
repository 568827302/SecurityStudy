package com.example.demo.utils;

import com.example.demo.config.AppProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtils {

    public static final Key signKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties properties;

    public String createSignJwtToken(UserDetails userDetails) {
        return createSignJwtToken(userDetails, System.currentTimeMillis());
    }

    public String createRefreshJwtToken(UserDetails userDetails) {
        return createRefreshJwtToken(userDetails, System.currentTimeMillis());
    }

    public String createSignJwtToken(UserDetails userDetails, long time) {
        return createJwtToken(userDetails, TokenType.SIGN_TOKEN, time);
    }

    public String createRefreshJwtToken(UserDetails userDetails, long time) {
        return createJwtToken(userDetails, TokenType.REFRESH_TOKEN, time);
    }

    private String createJwtToken(UserDetails userDetails, TokenType type, long time) {
        log.warn("invoke createJwtToken method: {} {} {}", userDetails, type, time);
        JwtBuilder builder = Jwts.builder()
                .setId("mooc")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(time));
        switch(type) {
            case SIGN_TOKEN:
                builder = builder
                        .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .setExpiration(new Date(time + properties.getJwt().getSignTokenExpiredTime()))
                        .signWith(signKey, SignatureAlgorithm.HS512);
                break;
            case REFRESH_TOKEN:
                builder = builder
                        .setExpiration(new Date(time + properties.getJwt().getRefreshTokenExpiredTime()))
                        .signWith(refreshKey, SignatureAlgorithm.HS512);
                break;
            default:
        }
        log.warn("compact: {}", builder.compact());
        return builder.compact();
    }

}
