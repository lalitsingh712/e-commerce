package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.PaymentRequest;
import com.lalit.e_commerce.entity.Payment;
import com.lalit.e_commerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "APIs for payment processing")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Operation(
            summary = "Make payment",
            description = "Processes payment for an order"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Payment already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/order/{orderId}")
    public ResponseEntity<Payment> makePayment(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequest request){

        return ResponseEntity.ok(paymentService.makePayment(orderId,request));
    }

    @Operation(summary = "Get payment by order ID")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPayment(
           @Parameter(description = "Order ID") @PathVariable Long orderId){

        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
