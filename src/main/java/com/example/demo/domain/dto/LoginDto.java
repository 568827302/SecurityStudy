package com.example.demo.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
