package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.PaymentRequest;
import com.lalit.e_commerce.entity.Order;
import com.lalit.e_commerce.entity.OrderStatus;
import com.lalit.e_commerce.entity.Payment;
import com.lalit.e_commerce.entity.PaymentStatus;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.DuplicateResourceException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.OrderRepository;
import com.lalit.e_commerce.repository.PaymentRepository;
import com.lalit.e_commerce.service.PaymentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Payment makePayment(Long orderId, PaymentRequest request) {

        // 1. Find Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId)
                );

        // 2. Check existing payment FIRST (most specific, common case)
        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new DuplicateResourceException("Payment already exists for this order");
        }

        // 3. Check order status
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot make payment for cancelled order");
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new DuplicateResourceException("Order already paid and confirmed");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot modify delivered order");
        }

        // 4. Validate amount
        if (order.getTotalAmount() == null || order.getTotalAmount().signum() <= 0) {
            throw new BadRequestException("Invalid order amount for payment");
        }

        // 5. Create Payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId("TXN-" + UUID.randomUUID());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        // 6. Update Order
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPayment(payment);
        orderRepository.save(order);

        // 7. Save Payment
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for order: " + orderId)
                );
    }
}