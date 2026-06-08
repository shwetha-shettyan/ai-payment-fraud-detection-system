package com.payment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudResultEvent {

    private String paymentIntentId;

    private Double fraudScore;

    private String decision;

    private List<String> reason;
}
