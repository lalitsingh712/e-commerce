package com.lalit.e_commerce.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;  // CASH_ON_DELIVERY, CARD, UPI
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
}
