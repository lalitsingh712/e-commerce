package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUserId(Long userId);
}
