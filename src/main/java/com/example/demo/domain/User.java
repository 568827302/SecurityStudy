package com.example.demo.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = -516388632941361552L;
    private String username;
    private String password;
    private String email;
    private String name;
}
