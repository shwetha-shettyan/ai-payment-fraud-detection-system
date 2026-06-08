package com.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency")
public class Idempotency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Getter @Setter String id;

    @Column(nullable = false)
    private @Getter @Setter String idempotencyKey;

    private @Getter @Setter String operationType; // CREATE_PAYMENT / CONFIRM_PAYMENT

    private @Getter @Setter String requestHash;

    private @Getter @Setter String status; // IN_PROGRESS, COMPLETED

    @Column(columnDefinition = "TEXT")
    private @Getter @Setter String responseBody;

    private @Getter @Setter Integer statusCode;

    private @Getter @Setter LocalDateTime createdAt;
    private @Getter @Setter LocalDateTime updatedAt;
}

