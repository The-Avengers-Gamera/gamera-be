package com.avengers.gamera.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.avengers.gamera.util.SystemParam;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private final SystemParam systemParam;

    @Bean
    @ConditionalOnProperty(name = "system-param.aws-active",havingValue = "true")
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        String awsAccessKey = systemParam.getAwsAccessKey();
        String awsSecretKey = systemParam.getAwsSecretKey();

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                awsAccessKey,awsSecretKey)))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

}