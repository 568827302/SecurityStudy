package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    int countByUsername(String username);
    int countByEmail(String email);
    int countByMobile(String mobile);
    Optional<User> findOptionalByUsername(String username);
}
