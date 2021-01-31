package com.example.demo.security.auth.ldap;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LDAPUserRepo extends LdapRepository<LDAPUser> {

    Optional<LDAPUser> findOptionalByUsernameAndPassword(String username, String password);
}
