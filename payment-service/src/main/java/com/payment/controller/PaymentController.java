package com.payment.controller;

import com.payment.dto.ConfirmPaymentRequest;
import com.payment.dto.CreatePaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.entity.Payment;
import com.payment.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @PathVariable String id,
            @RequestBody ConfirmPaymentRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        return ResponseEntity.ok(
                paymentService.confirmPayment(id, request, idempotencyKey)
        );
    }



}

