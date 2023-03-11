package com.avengers.gamera.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Getter
@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class SystemParam {
    @Value("${system-param.aws-access-key}")
    private String awsAccessKey;

    @Value("${system-param.aws-secret-key}")
    private String awsSecretKey;

    @Value("${system-param.sender-email}")
    private String senderEmail;

    @Value("${system-param.base-url}")
    private String baseUrl;

    @Value("${system-param.aws-active}")
    private String awsActive;

    @Value("${system-param.sign-in-jwt-expired-minute}")
    private String signInJwtExpiredMinute;

}