package com.avengers.gamera.service;


import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JWTService {

    public String createJWT (String email, Collection<? extends GrantedAuthority>  authorities, Long userId, Key secretKey){

        return Jwts.builder()
            .setSubject(email)
            .claim("authorities", authorities)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
            .signWith(secretKey)
            .compact();
    }

}

