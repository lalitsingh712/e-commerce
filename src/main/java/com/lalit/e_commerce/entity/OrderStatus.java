package com.lalit.e_commerce.entity;

import lombok.*;

@Getter
public enum OrderStatus {

    PENDING("Pending", "Order is pending confirmation"),
    CONFIRMED("Confirmed", "Order confirmed by seller"),
    PROCESSING("Processing", "Order is being processed"),
    SHIPPED("Shipped", "Order has been shipped"),
    OUT_FOR_DELIVERY("Out for Delivery", "Order is out for delivery"),
    DELIVERED("Delivered", "Order has been delivered"),
    CANCELLED("Cancelled", "Order has been cancelled"),
    RETURNED("Returned", "Order has been returned");

    private final String displayName;
    private final  String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
