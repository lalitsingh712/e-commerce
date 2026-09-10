package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.response.OrderResponse;
import com.lalit.e_commerce.entity.Order;
import com.lalit.e_commerce.entity.OrderStatus;
import com.lalit.e_commerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "APIs for order management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Place a new order",
            description = "Converts cart items to an order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/user/{userId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> placeOrder(
            @Parameter(description = "User ID") @PathVariable Long userId,
                                @RequestParam String shippingAddress){

        OrderResponse response=orderService.placeOrder(userId, shippingAddress);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "User ID") @PathVariable Long orderId,
            @RequestParam String status){

        OrderStatus newStatus=OrderStatus.valueOf(status.toUpperCase());
        OrderResponse response=orderService.updateOrderStatus(orderId,newStatus);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get order details with tracking")
    @GetMapping("/{orderId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order ID") @PathVariable Long orderId){

        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "Get user orders with tracking info")
    @GetMapping("/user/{userId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
            @Parameter(description = "User ID") @PathVariable Long userId){

        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @Operation(
            summary = "Cancel order",
            description = "Cancels an order if it hasn't been delivered"
    )
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @Parameter(description = "Order ID") @PathVariable Long orderId){

        orderService.cancelOrder(orderId);

        return ResponseEntity.noContent().build();
    }


}
