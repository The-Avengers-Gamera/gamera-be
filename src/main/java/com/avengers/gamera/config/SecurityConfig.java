package com.avengers.gamera.config;

import com.avengers.gamera.auth.AuthEntryPoint;
import com.avengers.gamera.auth.GameraAccessDeniedHandler;
import com.avengers.gamera.auth.GameraUserDetailService;
import com.avengers.gamera.jwt.JwtConfig;
import com.avengers.gamera.jwt.JwtTokenVerifyFilter;
import com.avengers.gamera.jwt.JwtUsernameAndPasswordAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Setter
@ConfigurationProperties(prefix = "cors")
public class SecurityConfig {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;

    private final GameraUserDetailService gameraUserDetailService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final JwtTokenVerifyFilter jwtTokenVerifyFilter;

    private static final String[] AUTH_URL_WHITELIST = {
            "/actuator",
            "/actuator/health",
            "/users/signup",
            "/users/login",
            "/verification/emails/**",
            "/articles/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(allowedOrigins);
                    cors.setAllowedMethods(allowedMethods);
                    cors.setAllowedHeaders(allowedHeaders);
                    cors.setExposedHeaders(exposedHeaders);
                    return cors;
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorize ->
                        authorize
                                .antMatchers(AUTH_URL_WHITELIST).permitAll()
                                .antMatchers("/reviews/**").hasAuthority("ROLE_EDITOR_REVIEW")
                                .antMatchers("/news/**").hasAuthority("ROLE_EDITOR_NEWS")
                                .anyRequest().authenticated())
                .addFilter(new JwtUsernameAndPasswordAuthFilter(authenticationManager(), secretKey, jwtConfig))
                .addFilterAfter(jwtTokenVerifyFilter, JwtUsernameAndPasswordAuthFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new GameraAccessDeniedHandler())
                .authenticationEntryPoint(new AuthEntryPoint())
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(gameraUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }
}
