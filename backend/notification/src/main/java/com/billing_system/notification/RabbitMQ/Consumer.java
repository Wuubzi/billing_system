package com.billing_system.notification.RabbitMQ;

import com.billing_system.notification.Services.EmailService;
import com.billing_system.notification.Services.SmsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private final SmsService smsService;
    private final EmailService emailService;

    public Consumer(SmsService smsService,
                    EmailService emailService){
        this.smsService = smsService;
        this.emailService = emailService;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "verification-code-queue")
    public void listen(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String code = jsonNode.get("code").asText();
            String phoneNumber = jsonNode.get("phone_number").asText();
            String messageText = "Your verification code is: " +  code + " Please enter this code to complete your verification. Do not share this code with anyone. Thank you!";
            smsService.sendSms(phoneNumber, messageText);
        } catch (Exception e) {
            System.err.println("Error al procesar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "reset-password-code-queue")
    public void listenPassword(String message) {
        try {
         JsonNode jsonNode = objectMapper.readTree(message);
         String code = jsonNode.get("code").asText();
         String email = jsonNode.get("email").asText();
         emailService.sendRestPasswordEmail(email,code);
        } catch (Exception e) {
            System.err.println("Error al procesar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}