package com.example.demo.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String name;
}
