package com.lalit.e_commerce.repository;

import com.lalit.e_commerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {


    Optional<Payment> findByOrderId(Long orderId);
}
