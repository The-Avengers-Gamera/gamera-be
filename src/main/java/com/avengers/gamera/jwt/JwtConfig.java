package com.avengers.gamera.jwt;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtConfig {
    private String secretKey;

    public String getAuthorization() {
        return HttpHeaders.AUTHORIZATION;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
