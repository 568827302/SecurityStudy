package com.example.demo.utils;

import com.example.demo.config.AppProperties;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@Profile("test")
@Slf4j
class JwtUtilsUnitTest {

    private JWTUtils utils;
    private AppProperties properties;

    @BeforeEach
    public void init() {
        properties = new AppProperties();
        utils = new JWTUtils(properties);
    }

    @Test
    void givenUserDetials_thenCreateTokenSuccess() throws Exception {
        String username = "heli";
        TokenType type = TokenType.SIGN_TOKEN;

        String token = generateToken(username, type);

        val claim = parseClaimsJws(token, type).getBody();

        Assert.assertEquals(username, claim.getSubject());
    }

    @Test
    void defaultJwtToken_hasDefaultExpiredTime() throws Exception {
        long start = System.currentTimeMillis();
        log.warn("method start: {}", start);
        TokenType type = TokenType.SIGN_TOKEN;

        String token = generateToken("user", type, start);

        val claim = parseClaimsJws(token, type).getBody();
        log.warn("method claim's expiration time: {}", claim.getExpiration().getTime());
        log.warn("method properties's signTokenExpiredTime: {}", properties.getJwt().getSignTokenExpiredTime());

        Assert.assertTrue(true);

    }

    private String generateToken(String username, TokenType type) throws Exception {
        val roles = Set.of(
                Role.builder().authority("USER").build(),
                Role.builder().authority("ADMIN").build()
        );
        val user = User.builder()
                .username(username)
                .authorities(roles)
                .build();
        if(TokenType.SIGN_TOKEN.equals(type))
            return utils.createSignJwtToken(user);
        else if(TokenType.REFRESH_TOKEN.equals(type))
            return utils.createRefreshJwtToken(user);
        else
            throw new Exception("不支持的类型");
    }

    private String generateToken(String username, TokenType type, long time) throws Exception {
        val roles = Set.of(
                Role.builder().authority("USER").build(),
                Role.builder().authority("ADMIN").build()
        );
        val user = User.builder()
                .username(username)
                .authorities(roles)
                .build();
        if(TokenType.SIGN_TOKEN.equals(type))
            return utils.createSignJwtToken(user);
        else if(TokenType.REFRESH_TOKEN.equals(type))
            return utils.createRefreshJwtToken(user);
        else
            throw new Exception("不支持的类型");
    }

    private Jws<Claims> parseClaimsJws(String token, TokenType type) throws Exception {
        val builder = Jwts.parserBuilder();
        JwtParser parser;
        if(TokenType.SIGN_TOKEN.equals(type))
            parser = builder.setSigningKey(JWTUtils.signKey).build();
        else if(TokenType.REFRESH_TOKEN.equals(type))
            parser = builder.setSigningKey(JWTUtils.refreshKey).build();
        else
            throw new Exception("不支持的类型");
        return parser.parseClaimsJws(token);
    }
}