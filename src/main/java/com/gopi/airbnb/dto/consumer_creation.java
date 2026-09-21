package com.AccountService.AccountMicroservice.services;

import com.AccountService.AccountMicroservice.dto.UserRegisteredEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserRegisteredEventConsumer {

    @KafkaListener(topics = "user-registered",groupId = "account-service-group")
    public void consumeUserRegisteredEvent(UserRegisteredEvent event) {

        System.out.println("User Registered Event Received:");
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Email: " + event.getEmail());
    }
}
