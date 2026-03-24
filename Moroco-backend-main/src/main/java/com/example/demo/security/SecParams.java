package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecParams {

    @Value("${jwt.expiration:3600}") // 1 heure par défaut
    private long expTime;

    @Value("${jwt.secret}")
    private String secret;

    public static final String PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    public long getExpTime() {
        return expTime;
    }

    public String getSecret() {
        return secret;
    }
}
