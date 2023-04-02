package com.avengers.gamera.service;

import com.avengers.gamera.config.ChatGptConfig;
import com.avengers.gamera.exception.ResourceNotFoundException;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptService {
    private final ChatGptConfig chatGptConfig;

    public String getChat(String prompt) {
        OpenAiService serviceG = chatGptConfig.service();
        CompletionRequest completionRequest = chatGptConfig.makeRequest(prompt);
        log.info("Sending request to OpenAI");
        try {
            CompletionResult completion = serviceG.createCompletion(completionRequest);
            return completion.getChoices().get(0).getText();
        } catch (Exception e) {
            log.info("Request OpenAI to create review failed: {}", e.getMessage());
            throw new ResourceNotFoundException("Request OpenAI to create review");
        }
    }
}
