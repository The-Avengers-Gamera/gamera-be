package com.avengers.gamera.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final SecretKey secretKey;

    public String createJWT (String email, Collection<? extends GrantedAuthority>  authorities, Long userId, Key secretKey, Date expireDate){

        return Jwts.builder()
            .setSubject(email)
            .claim("authorities", authorities)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(secretKey)
            .compact();
    }

    public Long decodeJWT(String token) {
        Claims jwt=Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        Integer userId= (Integer) jwt.get("userId");

        return longValue(userId);
    }

}

