package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.OrderResponse;
import com.lalit.e_commerce.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;

    public OrderMapper(OrderItemMapper orderItemMapper, PaymentMapper paymentMapper) {
        this.orderItemMapper = orderItemMapper;
        this.paymentMapper = paymentMapper;
    }

    public OrderResponse toResponse(Order order){

        OrderResponse response=new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().toString());

        response.setItems(
                order.getOrderItems().stream()
                        .map(orderItemMapper::toResponse)
                        .toList()
        );

        if(order.getPayment()!=null){
            response.setPayment(paymentMapper.toResponse(order.getPayment()));
        }

        return response;
    }
}
