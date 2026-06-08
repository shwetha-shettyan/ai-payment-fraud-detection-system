package com.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {

    private String paymentIntentId;

    private String userId;

    private Double amount;

    private String currency;

    private String paymentMethod;

    private String merchant;

    private String ipAddress;

    private String deviceId;

    private Long timestamp;
}


