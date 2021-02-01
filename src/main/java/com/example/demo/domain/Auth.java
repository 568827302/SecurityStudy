package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.io.Serializable;

@Data
@Builder
public class Auth implements Serializable {
    private String signToken;
    private String refreshToken;
}
