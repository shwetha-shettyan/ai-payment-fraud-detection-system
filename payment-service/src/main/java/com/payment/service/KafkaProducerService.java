package com.payment.service;

import com.payment.dto.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send("payment-topic", event)
                .thenAccept(result -> System.out.println("Sent to Kafka: " + event))
                .exceptionally(ex -> {
                    System.out.println("Kafka send failed: " + ex.getMessage());
                    return null;
                });
    }
}

