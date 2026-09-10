package com.lalit.e_commerce.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemRequest {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long id;
    private Long productId;
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
