package com.payment.dto;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {

    private String paymentIntentId;

    private String paymentMethod;

    private String ipAddress;

    private String deviceId;
}

