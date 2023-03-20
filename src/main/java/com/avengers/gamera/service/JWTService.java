package com.avengers.gamera.service;

import com.avengers.gamera.util.SystemParam;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
    private final SystemParam systemParam;

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

    public Claims decodeJWT (String token){
       return Jwts.parserBuilder().
               setSigningKey(Keys.hmacShaKeyFor(systemParam.getJwtSecretKey().getBytes()))
                .build()
                .parseClaimsJws(token).getBody();

    }
}

