package com.lalit.e_commerce.dto.request;

import com.lalit.e_commerce.entity.Cart;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartRequest {

    private Long id;
    private Long userId;
    private List<CartItemRequest> items;
    private BigDecimal totalPrice;
    private Integer itemCount;
}
