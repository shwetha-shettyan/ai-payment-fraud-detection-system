package com.payment.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String userId;
    private Long amount;
    private String currency;
    private String paymentMethod;

}
