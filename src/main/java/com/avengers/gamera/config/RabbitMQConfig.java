package com.avengers.gamera.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ARTICLE_CHATGPT_QUEUE_NAME = "article_chatgpt_queue";
    public static final String EMAIL_VERIFY_QUEUE_NAME = "email_verify_queue";

    @Bean
    public Queue articleChatGptQueue() {
        return new Queue(ARTICLE_CHATGPT_QUEUE_NAME);
    }

    @Bean
    public Queue emailVerifyQueue() {
        return new Queue(EMAIL_VERIFY_QUEUE_NAME);
    }

    // TODO: bind routing key later if we add more queue e.g email service
//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange("directExchange");
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with("routingKey");
//    }
}
