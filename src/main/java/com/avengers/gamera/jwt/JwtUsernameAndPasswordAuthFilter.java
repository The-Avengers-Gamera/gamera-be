package com.avengers.gamera.jwt;


import com.avengers.gamera.auth.GameraUserDetails;
import com.avengers.gamera.dto.user.UserInfoDto;
import com.avengers.gamera.service.JWTService;
import com.avengers.gamera.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.Collection;

public class JwtUsernameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_URL = "/users/login";
    private static final String BEARER = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    private final JWTService jwtService;


    public JwtUsernameAndPasswordAuthFilter(AuthenticationManager authenticationManager, SecretKey secretKey, JwtConfig jwtConfig, UserService userService, JWTService jwtService) {
        super.setFilterProcessesUrl(LOGIN_URL);
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
        this.jwtService=jwtService;
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

        String jwtToken = jwtService.createJWT(email,authorities,userId,secretKey);

        UserInfoDto userInfo = userService.getUserInfo(email);
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfoJson = objectMapper.writeValueAsString(userInfo);

        response.addHeader(jwtConfig.getAuthorization(), BEARER + jwtToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(userInfoJson);
        response.getWriter().flush();
        response.getWriter().close();
    }
}