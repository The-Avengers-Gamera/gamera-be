package com.avengers.gamera.service.EService;

public interface EmailService {
    void sendEmail(String receiverEmail, String link, String info);
}
