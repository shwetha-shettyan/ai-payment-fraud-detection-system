package com.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.dto.PaymentEvent;
import com.payment.entity.Payment;
import com.payment.entity.WebhookEvent;
import com.payment.dto.FraudResultEvent;
import com.payment.repository.PaymentRepository;
import com.payment.repository.WebhookEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaConsumerService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WebhookEventRepository webhookEventRepository;
    @Autowired
    private WebhookService webhookService;

    /* @KafkaListener(topics = "payment-topic", groupId = "payment-group-New")
    public void consume(PaymentEvent event) {
        System.out.println("#### Received event : " + event.getPaymentIntentId());

        Optional<Payment> optionalPayment = paymentRepository.findByPaymentIntentId(event.getPaymentIntentId());

        if (optionalPayment.isEmpty()) {
            System.out.println("Payment not found for id: " + event.getPaymentIntentId());
            return; // prevents infinite retry
        }

        Payment payment = optionalPayment.get();

        // Mark payment as PROCESSING 
        payment.setStatus("PROCESSING"); 
        payment.setUpdatedAt(LocalDateTime.now()); 
        paymentRepository.save(payment); 
        
        System.out.println( "Payment moved to PROCESSING state: " + payment.getPaymentIntentId() );

    } */

    @KafkaListener(topics = "fraud-result-topic", groupId = "fraud-result-group")
    public void consumeFraudResult(FraudResultEvent event) {

        System.out.println(
                "#### Fraud result received for payment : "
                        + event.getPaymentIntentId()
        );

        Optional<Payment> optionalPayment =
                paymentRepository.findByPaymentIntentId(
                        event.getPaymentIntentId()
                );

        if (optionalPayment.isEmpty()) {

            System.out.println(
                    "Payment not found for fraud result: "
                            + event.getPaymentIntentId()
            );

            return;
        }

        Payment payment = optionalPayment.get();
        payment.setFraudScore(event.getFraudScore());
        payment.setFraudDecision(event.getDecision());

        if (event.getReason() != null && !event.getReason().isEmpty()) {
            payment.setFailureReason( 
                String.join(", ", event.getReason()) 
            ); 
        }

        if ("BLOCKED".equals(event.getDecision())) {
            payment.setStatus("BLOCKED");
            payment.setFailureReason("Fraud suspected");
        } 
        else if ("REVIEW".equals(event.getDecision())) {
            payment.setStatus("REVIEW");
        } 
        else {
            payment.setStatus("SUCCESS");
        }

        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Create webhook event
        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setPaymentIntentId(
                payment.getPaymentIntentId()
        );
        webhookEvent.setEventType(payment.getStatus());
        webhookEvent.setPayload(
                "{\"paymentIntentId\":\""
                        + payment.getPaymentIntentId()
                        + "\", \"status\":\""
                        + payment.getStatus()
                        + "\"}"
        );
        webhookEvent.setStatus("PENDING");
        webhookEvent.setRetryCount(0);
        webhookEvent.setCreatedAt(LocalDateTime.now());
        webhookEventRepository.save(webhookEvent);

        // Trigger webhook
        webhookService.sendWebhook(webhookEvent);

        System.out.println(
                "Fraud decision applied : "
                        + payment.getStatus()
        );
    }
}

