package com.example.demo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mooc")
@Configuration
@Data
public class AppProperties {

    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private String header = "Authorization";
        private String prefix = "Bearer";
        private Long signTokenExpiredTime = 60_000L;
        private Long refreshTokenExpiredTime = 30 * 24 * 60 * 60_000L;
    }
}
