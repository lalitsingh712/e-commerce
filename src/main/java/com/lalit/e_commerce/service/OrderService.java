package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.response.OrderResponse;
import com.lalit.e_commerce.entity.Order;
import com.lalit.e_commerce.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(Long userId,String shippingAddress);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getOrdersByUser(Long userId);

    void cancelOrder(Long orderId);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
}
