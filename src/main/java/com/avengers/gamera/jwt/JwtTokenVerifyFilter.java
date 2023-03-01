package com.avengers.gamera.jwt;

import com.avengers.gamera.auth.GameraAuthenticationToken;
import com.avengers.gamera.auth.GameraUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenVerifyFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORITY = "authority";
    private static final String AUTHORITIES = "authorities";

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final GameraUserDetailService gameraUserDetailService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(jwtConfig.getAuthorization());

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace(BEARER, "");

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String email = body.getSubject();
            long userId = (Integer) body.get("userId");
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get(AUTHORITIES);

            Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(map -> new SimpleGrantedAuthority(map.get(AUTHORITY)))
                    .collect(Collectors.toSet());

            UserDetails userDetails = gameraUserDetailService.loadUserByUsername(email);

            GameraAuthenticationToken authentication = new GameraAuthenticationToken(
                    userId,
                    userDetails,
                    null,
                    grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}