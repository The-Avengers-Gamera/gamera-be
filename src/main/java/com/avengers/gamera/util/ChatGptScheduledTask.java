package com.avengers.gamera.util;

import com.avengers.gamera.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatGptScheduledTask {
    private final ArticleService articleService;

    // TODO: replace new openai api token
    //    @Scheduled(fixedDelay = 1000 * 60 * 10)
    public void scheduledTask() {
        log.info("Scheduled task create review by chatgpt is running");
        try {
            articleService.createByChatGpt();
        } catch (Exception e) {
            log.error("Scheduled task create review by chatgpt failed");
        }
    }
}
