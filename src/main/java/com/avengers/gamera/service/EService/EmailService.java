package com.avengers.gamera.service.EService;

import org.springframework.scheduling.annotation.Async;

@Async
public interface EmailService {
    void sendEmail(String receiverEmail, String link, String info);
}
