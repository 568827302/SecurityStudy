package com.example.demo.utils;

import com.example.demo.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtils {

    public static final Key ACCESS_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key REFRESH_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties properties;

    /**
     * 根据UserDetails创建访问令牌
     */
    public String createAccessToken(UserDetails userDetails) {
        return createToken(userDetails, ACCESS_KEY, System.currentTimeMillis());
    }

    /**
     * 根据UserDetails创建刷新令牌
     */
    public String createRefreshToken(UserDetails userDetails) {
        return createToken(userDetails, REFRESH_KEY, System.currentTimeMillis());
    }

    /**
     * 校验访问令牌是否为本服务器签发
     */
    public boolean validAccessToken(String token) {
        return validToken(token, ACCESS_KEY, false);
    }

    /**
     * 校验刷新令牌是否为本服务器签发
     */
    public boolean validRefreshToken(String token) {
        return validToken(token, REFRESH_KEY, false);
    }

    /**
     * 校验刷新令牌是否为本服务器签发
     * @param token 令牌串
     * @return
     */
    public boolean validAccessTokenWithoutExpired(String token) {
        return validToken(token, ACCESS_KEY, true);
    }

    public String buildAccessTokenWithRefreshToken(String refreshToken) {
        return parseClaims(refreshToken, REFRESH_KEY)
                .map(claims -> Jwts.builder()
                        .setClaims(claims)
                        .signWith(ACCESS_KEY, SignatureAlgorithm.HS512)
                        .setExpiration(new Date(System.currentTimeMillis() + properties.getJwt().getAccessTokenExpiredTime()))
                        .compact())
                .orElseThrow();
    }

    private Optional<Claims> parseClaims(String token, Key key) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    private String createToken(UserDetails userDetails, Key key, long timeToExpire) {
        long currTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId("mooc")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currTime))
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setExpiration(new Date(currTime + timeToExpire))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    /**
     * 校验令牌是否本服务器签发，（可选）校验令牌是否过期
     * @param token 令牌
     * @param key 密钥
     * @param isExpiredInvolved 是否包含过期校验 true-过期令牌校验失败 false-过期令牌可通过校验
     * @return 是否通过校验
     */
    private boolean validToken(String token, Key key, boolean isExpiredInvolved) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return null != claims.get("authorities");

        } catch (UnsupportedJwtException| MalformedJwtException| SignatureException| IllegalArgumentException exc) {

            return false;
        } catch (ExpiredJwtException expiredJwtException) {
            return !isExpiredInvolved;
        }
    }
}
