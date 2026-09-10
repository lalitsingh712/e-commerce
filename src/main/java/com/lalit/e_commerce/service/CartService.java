package com.lalit.e_commerce.service;

import com.lalit.e_commerce.entity.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);

    Cart createCart(Long userId);

    Cart addProductToCart(
            Long userId,
            Long productId,
            Integer quantity
    );

    Cart updateCartItem(
            Long userId,
            Long productId,
            Integer quantity
    );

    void removeProductFromCart(
            Long userId,
            Long productId
    );

    void clearCart(Long userId);
}
