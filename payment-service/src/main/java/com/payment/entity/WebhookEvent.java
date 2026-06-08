package com.payment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_event")
@Data
public class WebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentIntentId;

    private String eventType; // SUCCESS / FAILED

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String status; // PENDING / SENT / FAILED

    private int retryCount;

    private LocalDateTime createdAt;

}

