package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.OrderItemResponse;
import com.lalit.e_commerce.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemResponse toResponse(OrderItem orderItem){

        OrderItemResponse response=new OrderItemResponse();

        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setSubtotal(orderItem.getSubtotal());

        return response;
    }
}
