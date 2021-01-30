package com.example.demo.security.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * final 标记的类属性一定只能在构造函数内初始化， 所以用@RequiredArgsConstructor注释，
     * 默认lombok会在构造函数内注入ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 按照AbstractAuthenticationProcessingFilter内的抽象方法说明
     * 如果认证成功则返回登陆成功的Authentication令牌 如果失败则返回null 如果认证过程失败则抛出AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 仿照 UsernamePasswordAuthenticationFilter部分实现
        String username = null;
        String password = null;

        try {
            InputStream is = request.getInputStream();
            val node = objectMapper.readTree(is);
            username = node.get("username").asText();
            password = node.get("password").asText();
        } catch (IOException e) {
            log.error("The Request InputStream Get Some Wrong!", e);
            throw new BadCredentialsException(e.getMessage());
        }
        
		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
        

		return this.getAuthenticationManager().authenticate(authRequest);
        
    }

}
