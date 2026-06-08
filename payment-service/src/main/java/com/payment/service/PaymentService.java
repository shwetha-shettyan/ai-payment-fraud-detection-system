package com.payment.service;

import com.payment.dto.ConfirmPaymentRequest;
import com.payment.dto.CreatePaymentRequest;
import com.payment.dto.PaymentEvent;
import com.payment.dto.PaymentResponse;
import com.payment.entity.Idempotency;
import com.payment.entity.Payment;
import com.payment.repository.IdempotencyRepository;
import com.payment.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private IdempotencyRepository idempotencyRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Payment payment = new Payment();
        payment.setPaymentIntentId(UUID.randomUUID().toString());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus("CREATED");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return new PaymentResponse(payment.getPaymentIntentId(), payment.getStatus());
    }

    public Payment getPayment(String paymentIntentId) {
        return paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Transactional
    public PaymentResponse confirmPayment(String paymentIntentId, ConfirmPaymentRequest request, String idempotencyKey) {

        // Step 1: Check idempotency
        if (idempotencyKey != null) {
            Optional<Idempotency> existing = idempotencyRepository
                    .findByIdempotencyKeyAndOperationType(idempotencyKey, "CONFIRM_PAYMENT");

            if (existing.isPresent()) {
                Idempotency record = existing.get();

                if ("COMPLETED".equals(record.getStatus())) {
                    // Return cached response
                    return parseResponse(record.getResponseBody());
                } else {
                    throw new RuntimeException("Request already in progress");
                }
            }
        }

        // Step 2: Insert IN_PROGRESS
        Idempotency idempotency = new Idempotency();
        idempotency.setIdempotencyKey(idempotencyKey);
        idempotency.setOperationType("CONFIRM_PAYMENT");
        idempotency.setStatus("IN_PROGRESS");
        idempotency.setCreatedAt(LocalDateTime.now());
        idempotency.setUpdatedAt(LocalDateTime.now());

        idempotencyRepository.save(idempotency);

        // Step 3: Normal processing (your existing logic)
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // State validation
        if (!payment.getStatus().equals("CREATED")) {
            throw new RuntimeException("Invalid state");
        }

        // Move to PROCESSING
        payment.setStatus("PROCESSING");
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setIpAddress(request.getIpAddress()); 
        payment.setDeviceId(request.getDeviceId());
        paymentRepository.save(payment);


        // Step 2: publish event
        PaymentEvent event = PaymentEvent.builder()
            .paymentIntentId(payment.getPaymentIntentId())
            .userId(payment.getUserId())
            .amount(payment.getAmount().doubleValue())
            .currency(payment.getCurrency())
            .paymentMethod(payment.getPaymentMethod())
            .merchant("AMAZON")
            .ipAddress(request.getIpAddress())
            .deviceId(request.getDeviceId())
            .timestamp(System.currentTimeMillis())
            .build();

        /*TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        kafkaProducerService.sendPaymentEvent(event);
                    }
                }
        );*/

        kafkaProducerService.sendPaymentEvent(event);

        // Step 3: return immediately
        return new PaymentResponse(payment.getPaymentIntentId(), payment.getStatus());
    }

    private String toJson(PaymentResponse response) {
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion failed");
        }
    }

    private PaymentResponse parseResponse(String json) {
        try {
            return new ObjectMapper().readValue(json, PaymentResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing failed");
        }
    }

}

