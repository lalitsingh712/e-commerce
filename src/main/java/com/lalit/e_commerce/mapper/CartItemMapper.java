package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.CartItemResponse;
import com.lalit.e_commerce.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemResponse toResponse(CartItem cartItem){

        CartItemResponse response=new CartItemResponse();

        response.setId(cartItem.getId());
        response.setQuantity(cartItem.getQuantity());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setPrice(cartItem.getProduct().getPrice());
        response.setSubTotal(
                cartItem.getProduct().getPrice()
                        .multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity()))
        );
        return response;
    }
}
