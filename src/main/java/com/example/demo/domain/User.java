package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "mooc_users")
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50, nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @Column(length = 150, nullable = false, name = "password_hash")
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false, name = "credentials_non_expired")
    private boolean credentialsNonExpired;
    @Column(nullable = false, name = "account_non_locked")
    private boolean accountNonLocked;
    @Column(nullable = false, name = "account_non_expired")
    private boolean accountNonExpired;
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 50)
    private String name;
    @Column(length = 30)
    private String mobile;

    @ManyToMany
    @Fetch(FetchMode.JOIN)      // 优化查询，查询User的时候回join直接查出来Role，不用查询两次表
    @JoinTable( // 建立中间关联表
            name = "mooc_users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> authorities;
}
