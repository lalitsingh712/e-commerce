package com.lalit.e_commerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private BigDecimal totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


}
