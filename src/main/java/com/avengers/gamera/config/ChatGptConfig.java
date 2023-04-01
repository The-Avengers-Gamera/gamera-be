package com.avengers.gamera.config;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "open-ai")
@Getter
@Setter
@Slf4j
public class ChatGptConfig {
    private String apiKey;
    private String model;
    private Integer maxToken;
    private Double temperature;
    private Double topP;

    @Bean
    public OpenAiService service() {
        return new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public CompletionRequest makeRequest(String prompt) {
        return CompletionRequest.builder()
                .model(model)
                .prompt(prompt)
                .temperature(temperature)
                .maxTokens(maxToken)
                .topP(topP)
                .frequencyPenalty(0D)
                .presencePenalty(0D)
                .build();
    }
}
