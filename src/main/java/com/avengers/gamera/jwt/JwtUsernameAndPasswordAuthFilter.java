package com.avengers.gamera.jwt;

import com.avengers.gamera.auth.GameraUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUsernameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_URL = "/users/login";
    private static final String BEARER = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthFilter(AuthenticationManager authenticationManager, SecretKey secretKey, JwtConfig jwtConfig) {
        super.setFilterProcessesUrl(LOGIN_URL);
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        AuthenticationRequest authenticationRequest = new ObjectMapper()
                .readValue(request.getInputStream(), AuthenticationRequest.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        );

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String email = authResult.getName();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        long userId = ((GameraUserDetails) authResult.getPrincipal()).getId();

        String jwtToken = Jwts.builder()
                .setSubject(email)
                .claim("authorities", authorities)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(secretKey)
                .compact();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("authorities", authorities);
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfoJson = objectMapper.writeValueAsString(userInfo);

        response.addHeader(jwtConfig.getAuthorization(), BEARER + jwtToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(userInfoJson);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if (failed.getCause() instanceof InternalAuthenticationServiceException) {
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED),failed.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print("Login failed. Please try again.");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
