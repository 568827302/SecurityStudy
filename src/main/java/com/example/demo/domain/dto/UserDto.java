package com.example.demo.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDto implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "用户名长度必须在4~40个字符之间")
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 20, message = "密码长度必须在8~20个字符之间")
    private String password;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 20, message = "密码长度必须在8~20个字符之间")
    private String matchPassword;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "姓名长度必须在4~50个字符之间")
    private String name;
}
