package com.avengers.gamera.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final SecretKey secretKey;

    public String createJWT (String email, List<String> authorities, Long userId, Key secretKey){

        return Jwts.builder()
            .setSubject(email)
            .claim("authorities", authorities)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
            .signWith(secretKey)
            .compact();
    }

    public Long decodeJWT(String token) {
        Claims jwt=Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        Integer userId= (Integer) jwt.get("userId");

        return longValue(userId);
    }
}

