package com.avengers.gamera.service.EmailService;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.avengers.gamera.util.SystemParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@ConditionalOnProperty(name = "system-param.aws-active",havingValue = "true")
public record AmazonEmailService (AmazonSimpleEmailService amazonSimpleEmailService, SystemParam systemParam) implements EmailService {

    @Override
    public void sendEmail(String receiverEmail, String link, String info) {
        String senderEmail = systemParam.getSenderEmail();
        Destination destination = new Destination();
        List<String> toAddresses = List.of(receiverEmail);
        destination.withToAddresses(toAddresses);
        Message message=new Message();
        message.withSubject(new Content("Welcome Gamera"));
        message.withBody(new Body(new Content(info+": Nice to meet you, please active your account with below link: "+
                link)));

        System.out.println(senderEmail);
        System.out.println(receiverEmail);

        SendEmailRequest sendEmailRequest= new SendEmailRequest();
        sendEmailRequest.withDestination(destination)
                .withMessage(message)
                .withSource(senderEmail);

        try{
            SendEmailResult result= amazonSimpleEmailService.sendEmail(sendEmailRequest);
            System.out.println(result);
        }
        catch (Exception e){

            System.out.println(e);

        }
    }
}