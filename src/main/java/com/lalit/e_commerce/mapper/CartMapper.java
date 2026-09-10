package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.CartResponse;
import com.lalit.e_commerce.entity.Cart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    public CartResponse toResponse(Cart cart){

        CartResponse response=new CartResponse();

        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setItems(
                cart.getCartItems().stream()
                        .map(cartItemMapper::toResponse)
                        .toList()
        );

        java.math.BigDecimal totalPrice=cart.getCartItems().stream()
                .map(item->item.getProduct().getPrice()
                        .multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO,java.math.BigDecimal::add);

        response.setTotalPrice(totalPrice);

        return response;
    }
}
