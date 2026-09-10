package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.response.OrderResponse;
import com.lalit.e_commerce.entity.*;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.DuplicateResourceException;
import com.lalit.e_commerce.exception.InsufficientStockException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.CartRepository;
import com.lalit.e_commerce.repository.OrderRepository;
import com.lalit.e_commerce.repository.ProductRepository;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private  OrderRepository orderRepo;
    @Autowired
    private  CartRepository cartRepo;
    @Autowired
    private  UserRepository userRepo;
    @Autowired
    private  ProductRepository productRepo;



    @Override
    @Transactional
    public OrderResponse placeOrder(Long userId, String shippingAddress) {
        log.info("Placing order for user: {}",userId);
        // Validate user exists
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Get cart
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart not found for user: " + userId));

        // Validate cart is not empty
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cannot place order because cart is empty");
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setTrackingNumber(generateTrackingNumber());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // Convert CartItems to OrderItems
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            // Validate stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName()
                                + ". Available: " + product.getStockQuantity()
                                + ", Requested: " + cartItem.getQuantity()
                );
            }


            // Get current product price
            BigDecimal price = product.getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(subtotal);

            // Reduce stock and save product
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepo.save(product);
        }

        // Set order items and total
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save order (cascades to orderItems)
        Order savedOrder = orderRepo.save(order);

        // Clear cart
        cart.getCartItems().clear();
        cartRepo.save(cart);

        log.info("Order placed successfully: {}",savedOrder.getId());

        return convertToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId,OrderStatus newStatus){

        Order order=orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus=order.getStatus();

        // Validate status transition
        validateStatusTransition(currentStatus,newStatus);

        // Update status based on new status
        order.setStatus(newStatus);

        switch (newStatus){
            case SHIPPED:
                order.setShippedDate(LocalDateTime.now());
                log.info("Order {} shipped", orderId);
                break;
            case DELIVERED:
                order.setDeliveredDate(LocalDateTime.now());
                log.info("Order {} delivered", orderId);
                break;
            case PROCESSING:
                log.info("Order {} is being processed", orderId);
                break;
            case CANCELLED:
                if(order.getStatus() != OrderStatus.CONFIRMED){
                    throw new BadRequestException("Can only cancel confirm orders");
                }
                //Refund stock
                for (OrderItem item : order.getOrderItems()){
                    Product product=item.getProduct();
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                    productRepo.save(product);
                }
                break;
        }

        Order updateOrder=orderRepo.save(order);
        log.info("Order {} status updated to {}",orderId,newStatus);
        return convertToResponse(updateOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order=orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return convertToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {

        userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        return orderRepo.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    //Helper method to generate unique tracking number
    private String generateTrackingNumber(){
        return "TRK-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,8);
    }

    //Validate status transitions
    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        if (from == OrderStatus.CANCELLED || from == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot change status of " + from + " order");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Delivered order cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new DuplicateResourceException("Order is already cancelled");
        }

        //Cannot cancel if payment completed
        if (order.getPayment() != null &&
                order.getPayment().getPaymentStatus().equals("COMPLETED")) {
            throw new BadRequestException("Cannot cancel order with completed payment. Please request refund instead.");
        }

        //REFUND STOCK - Return items back to inventory
        for (OrderItem orderItem:order.getOrderItems()){
            Product product=orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity()+orderItem.getQuantity());
            productRepo.save(product);
            log.info("Refunded {} units of product: {}", orderItem.getQuantity(), product.getName());
        }

        // Update order status
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
        log.info("Order {} cancelled and stock refunded",orderId);
    }

    private void reduceStock(
            Product product,
            int quantity) {

        if (product.getStockQuantity() < quantity) {

            throw new InsufficientStockException(
                    "Insufficient stock for product: "
                            + product.getName()
            );
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );

        productRepo.save(product);
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
            order.getId(),
                order.getUser().getId(),
                order.getStatus().getDisplayName(),
                order.getTotalAmount(),
                order.getOrderDate(),
                order.getShippedDate(),
                order.getDeliveredDate(),
                order.getTrackingNumber(),
                order.getShippingAddress()
        );
    }
}