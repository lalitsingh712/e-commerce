package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartIdAndProductId(Long cartId,Long productId);
}
