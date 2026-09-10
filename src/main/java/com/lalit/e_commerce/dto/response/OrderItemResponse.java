package com.lalit.e_commerce.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
