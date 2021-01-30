package com.example.demo.config;

import com.example.demo.security.filter.RestAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.passay.MessageResolver;
import org.passay.spring.SpringMessageResolver;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import java.util.Map;

/**
 * BasicAuthenticationFilter    如果在请求中找到一个 Basic Auth HTTP请求头, 那么尝试从其中获取username和password
 * UsernamePasswordAuthenticationFilter 表单请求时候在post请求的body内获取username和password
 * DefaultLoginPageGeneratingFilter 默认登录页面生成
 * DefaultLogoutPageGeneratingFilter 默认登出
 * FilterSecurityInterceptor    安全过滤器，用于授权逻辑
 */
@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
@Order(99)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Bean
    public MessageResolver messageResolver() {
        return new SpringMessageResolver(messageSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/authorize/**", "/admin/**", "/api/**"))   // 对这些地址
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   // session管理策略变为无状态
            .authorizeRequests(req -> 
                req
                    .antMatchers("/authorize/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/api/**").hasRole("USER")
                    .anyRequest().authenticated()
            )
//                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)    // 替换掉原来的UsernamePasswordAuthenticationFilter验证功能
                .addFilterBefore(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .csrf(csrf -> csrf.ignoringAntMatchers("/authorize/**", "/admin/**", "/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
        ;

    }

    /**
     * 有的请求不希望经过整个FilterChain，可以简单的判断允许或者拒绝的，在这个配置内设置
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/error/**")   // 比如请求/public/** 的请求都直接忽略，允许请求
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());  // 对常见的静态资源的映射加到requestMapping
    }

    /**
     * 请求如果经过这个configure，会经过整个FilterChain，开销十分可观
     */
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     // http.authorizeRequests(req -> req.mvcMatchers("/api/greeting").hasRole("ADMIN"));   // 需要权限才能访问，这样直接访问/api/greeting的话会反回HTTP 403 未授权
    //     // http
    //     //     .formLogin(Customizer.withDefaults())       // 这样就可以跳转到登录页了，因为重写了configure方法就要自己指定
    //     //     .authorizeRequests(req -> req.mvcMatchers("/api/greeting").authenticated());    // 改成需要认证才能访问

    //     http
    //         // .csrf(csrf -> 
    //     // csrf.csrfTokenRepository(HttpSessionCsrfTokenRepository) // csrf存在session
    //     // csrf.csrfTokenRepository(CookieCsrfTokenRepository)      // csrf存在cookie
    //     // csrf.ignoringAntMatchers("/api/**") // 忽略哪些url
    //     // csrf.ignoringRequestMatchers(new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON_UTF8))  // 能指定实现RequestMatcher接口的不同的方式。比如MediaType，Method等
    //     // csrf.requireCsrfProtectionMatcher("/index") // 对哪些需要保护
    //     // )
    //     // .csrf(csrf -> csrf.disable())
    //     .csrf(Customizer.withDefaults())
    //      /**
    //         CSRF 攻击：只对Session类站点有效，对无状态站点无效。非法劫持站点，然后植入外部连接，在用户登陆过后，点击外部连接进入仿照的支付页面，输入信息，可以用登陆后的sessionId模拟用户发送请求
    //             防护方式：
    //                 1）CSRF Token：在每次提交都提交 _csrf.parameterName:_csrf.token (机制是，只有真正的服务端给的支付页面才有真正的csrf token，非法站点无法提供)
    //                 2）在响应中设置Cookie的SameSite属性（但是这个机制需要浏览器支持【IE不支持】）
    //     */
    //     .rememberMe(rememberMe -> 
    //         rememberMe.tokenValiditySeconds(60*60*24*30)    // 指定有效时间
    //             .rememberMeCookieName("someKeyToRemember")  // 指定cookie名字
    //     )
    //     /**
    //      * rememberMe功能：为了解决session过期后用户直接访问的问题
    //      * 原理：使用Cookie存储用户名、过期时间、Hash值
    //      * Hash：md5（用户名+过期时间+密码+key）
    //      */
    //         .httpBasic(Customizer.withDefaults())
    //         // .formLogin(login -> login.loginPage("/"))
    //         // .formLogin(login -> login.disable())    // 禁用表单登陆
    //         .formLogin(login -> login.loginPage("/login")
    //                     // .loginProcessingUrl("/login")    // 默认 /login 也就是登陆form表单提交的action属性
    //                     .successHandler(restAuthenticationSuccessHandler())
    //                     .failureHandler(restAuthenticationFailureHandler())
    //                     .permitAll())    // 指定样式之后，默认登陆也就不显示了
    //         .logout(logout -> logout.logoutUrl("/perform_logout").logoutSuccessUrl("/")
    //                     .logoutSuccessHandler((req, rsp, auth) -> {
    //                         val mapper = new ObjectMapper();
    //                         rsp.setStatus(HttpStatus.OK.value());
    //                         rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    //                         rsp.setCharacterEncoding("UTF-8");
    //                         if(auth.isAuthenticated()) {
    //                             auth.setAuthenticated(false);
    //                             req.logout();
    //                         }
    //                         val map = Map.of("title", "登出成功");
    //                         rsp.getWriter().println(mapper.writeValueAsString(map));
    //                     }))
    //         // .authorizeRequests(req -> req.antMatchers("/api/**").authenticated());  // 拦截 /api/** 请求
    //         .authorizeRequests(req -> req.anyRequest().authenticated());  // 拦截 所有 请求

    // }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }





    @Bean
    public OptionalValidatorFactoryBean validatorFactoryBean() {
        val factoryBean = new OptionalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }

    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager());   // 把当前的authenticationManager传进去
        filter.setFilterProcessesUrl("/authorize/login");   // Filter拦截的url
        return filter;
    }

    private AuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return (req, rsp, auth) -> {
            ObjectMapper mapper = new ObjectMapper();
            rsp.setStatus(HttpStatus.OK.value());
            rsp.getWriter().println(mapper.writeValueAsString(auth));
        };
    }

    private AuthenticationFailureHandler restAuthenticationFailureHandler() {
        return (req, rsp, exception) -> {
            val mapper = new ObjectMapper();
            rsp.setStatus(HttpStatus.UNAUTHORIZED.value());
            rsp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            rsp.setCharacterEncoding("UTF-8");
            val map = Map.of("title", "认证失败", "reason", exception.getMessage());
            rsp.getWriter().println(mapper.writeValueAsString(map));
        };
    }
}
