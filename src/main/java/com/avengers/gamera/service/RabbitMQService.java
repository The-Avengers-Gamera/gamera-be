package com.avengers.gamera.service;

import com.avengers.gamera.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final ArticleService articleService;

    public void sendArticleChatGpt(String gameId) {
        log.info("Send message to create article by chatgpt to queue: {}", gameId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.ARTICLE_CHATGPT_QUEUE_NAME, gameId);
    }

    @RabbitListener(queues = RabbitMQConfig.ARTICLE_CHATGPT_QUEUE_NAME)
    public void receiveArticleChatGpt() {
        log.info("Received message to create article by chatgpt from queue");
        try {
            articleService.createByChatGpt();
        } catch (Exception e) {
            log.error("Error when create article by chatgpt", e);
        }
    }
}
