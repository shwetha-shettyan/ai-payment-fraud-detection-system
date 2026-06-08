package com.payment.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Getter @Setter String id;

    @Column(unique = true)
    private @Getter @Setter String paymentIntentId;

    private @Getter @Setter String userId;
    private @Getter @Setter Long amount;
    private @Getter @Setter String currency;
    private @Getter @Setter String status;
    private @Getter @Setter String paymentMethod;
    private @Getter @Setter String failureReason;

    private @Getter @Setter LocalDateTime createdAt;
    private @Getter @Setter LocalDateTime updatedAt;

    private @Getter @Setter Double fraudScore; 
    private @Getter @Setter String fraudDecision;

    private @Getter @Setter String ipAddress;
    private @Getter @Setter String deviceId;

}
