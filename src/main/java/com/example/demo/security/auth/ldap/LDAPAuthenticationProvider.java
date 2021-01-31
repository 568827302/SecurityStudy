package com.example.demo.security.auth.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class LDAPAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final LDAPUserRepo ldapUserRepo;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 检查相关流程，暂时忽略
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return ldapUserRepo.findOptionalByUsernameAndPassword(username, authentication.getCredentials().toString())
                .orElseThrow(() -> new BadCredentialsException("[LDAP] 用户名或密码错误"));
    }
}
