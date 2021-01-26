package com.example.demo.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.validation.annotation.ValidPassword;
import com.example.demo.validation.annotation.ValidPasswordMatch;

import lombok.Data;

@Data
@ValidPasswordMatch
public class UserDto implements Serializable {
    private static final long serialVersionUID = -6189226843467326983L;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "用户名长度必须在4~40个字符之间")
    private String username;
    @NotNull
    @ValidPassword
    private String password;
    @NotNull
    private String matchPassword;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "姓名长度必须在4~50个字符之间")
    private String name;
}
