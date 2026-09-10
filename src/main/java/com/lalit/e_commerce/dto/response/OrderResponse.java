package com.lalit.e_commerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;  // PENDING, CONFIRMED, DELIVERED, CANCELLED
    private List<OrderItemResponse> items;
    private PaymentResponse payment;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private String trackingNumber;
    private String shippingAddress;


    public OrderResponse(Long id, Long userId, String status, BigDecimal totalAmount,
                         LocalDateTime orderDate, LocalDateTime shippedDate, LocalDateTime deliveredDate,
                         String trackingNumber, String shippingAddress) {
        this.id=id;
        this.userId=userId;
        this.status=status;
        this.totalAmount=totalAmount;
        this.orderDate=orderDate;
        this.shippedDate=shippedDate;
        this.deliveredDate=deliveredDate;
        this.trackingNumber=trackingNumber;
        this.shippingAddress=shippingAddress;

    }
}
