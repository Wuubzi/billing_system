package com.billing_system.auth.RabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Producer {
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public Producer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    };

    public void sendMessage(String queue,String message){
        rabbitTemplate.convertAndSend(queue, message);
    }
    public void sendVerificationCode(String phone_number, String code) throws JsonProcessingException {
        Map<String, String> message = new HashMap<>();
        message.put("phone_number", phone_number);
        message.put("code", code);
        String jsonMessage = new ObjectMapper().writeValueAsString(message);
        rabbitTemplate.convertAndSend("verification-code-queue", jsonMessage);
    }

    public void sendResetPasswordCode(String email, String code) throws   JsonProcessingException {
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("code", code);
        String jsonMessage = new ObjectMapper().writeValueAsString(message);
        rabbitTemplate.convertAndSend("reset-password-code-queue", jsonMessage);
    }
}
