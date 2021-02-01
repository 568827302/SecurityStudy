package com.example.demo.security.jwt;

import com.example.demo.config.AppProperties;
import com.example.demo.utils.CollectionUtil;
import com.example.demo.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AppProperties properties;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(conditionalJwtHeader(request)) {
            validationToken(request)
                    .filter(rawL -> null != rawL.get("authorities"))    // 过滤掉没有authorities参数的
                    .ifPresentOrElse(
                            tokenIsValidAndPass(),
                            SecurityContextHolder::clearContext
                    );
        }

        filterChain.doFilter(request, response);
    }

    private Consumer<Claims> tokenIsValidAndPass() {
        return claims -> {
            val rawList = CollectionUtil.convertObjectToList(claims.get("authorities"));
            val authorities = rawList.stream()
                    .map(String::valueOf)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            val usernamePasswordToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
        };
    }

    /**
     * 获取请求内Header： Authorization: Bearer xxxxxxx 的xxxx组成Claims
     * @param request Http请求
     * @return 返回Claims的Optional
     */
    private Optional<Claims> validationToken(HttpServletRequest request) {
        String header = request.getHeader(properties.getJwt().getHeader()).trim();
        String token = header.replace(properties.getJwt().getPrefix(), "");
        return Optional.of(Jwts.parserBuilder().setSigningKey(JWTUtils.signKey)
                .build()
                .parseClaimsJws(token)
                .getBody());
    }

    /**
     * 判断请求头是否包含 Authorization: Bearer xxxxxxxx
     * @param request HttpServletRequest
     * @return 是否包含 Authorization: Bearer xxxxxxx
     */
    private boolean conditionalJwtHeader(HttpServletRequest request) {
        String header = request.getHeader(properties.getJwt().getHeader());
        return !StringUtils.isEmpty(header) // 请求头不为空
                && header.trim().startsWith(properties.getJwt().getPrefix());  // 请求头前缀与配置相同
    }
}
