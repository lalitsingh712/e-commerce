package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateOrderItemRepository extends JpaRepository<OrderItem,Long> {
}
