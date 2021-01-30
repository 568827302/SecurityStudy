package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(100) // value越小，优先级约高。优先级低的配置能覆盖优先级高的配置，如果WebSecurityConfigurerAdapter的重写方法
            // configure(AuthenticationManagerBuilder auth) 不重写，优先级低的会用默认实现覆盖掉优先级高的重写方法
@RequiredArgsConstructor
@Slf4j
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/index")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/perform_logout"))
//                .csrf(Customizer.withDefaults())
                .rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(30 * 24 * 3600).rememberMeCookieName("rememberHL"));
    }

    /**
     * 有的请求不希望经过整个FilterChain，可以简单的判断允许或者拒绝的，在这个配置内设置
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/error/**");   // 比如请求/public/** 的请求都直接忽略，允许请求
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());  // 对常见的静态资源的映射加到requestMapping
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String credential = passwordEncoder.encode("12345678");
        log.info("encode后密码 {}", credential);
        credential = passwordEncoder.encode("12345678");
        log.info("encode后密码 {}", credential);

        log.info("try {}", passwordEncoder.matches("12345678", credential));
//
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(credential)
////                .roles("USER", "ADMIN");
//                .roles("USER");
        auth.inMemoryAuthentication()
            .withUser(User.builder()
                    .username("user")
                    .password(credential)
                    .roles("USER")
                    .build());
    }
}
