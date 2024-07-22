package org.example.mobilephonebookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message) {
        kafkaTemplate.send("phoneNotifications", message);
    }
}
