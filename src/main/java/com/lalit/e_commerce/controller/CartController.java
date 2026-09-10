package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.CartItemRequest;
import com.lalit.e_commerce.dto.request.CartRequest;
import com.lalit.e_commerce.dto.response.CartItemResponse;
import com.lalit.e_commerce.dto.response.CartResponse;
import com.lalit.e_commerce.entity.Cart;
import com.lalit.e_commerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Shopping Cart", description = "APIs for managing shopping carts")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Create cart", description = "Creates a new cart for a user")
    @PostMapping("/user/{userId}")
    public ResponseEntity<CartResponse> createCart(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        Cart cart = cartService.createCart(userId);
        return new ResponseEntity<>(convertToCartResponse(cart), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get user cart",
            description = "Retrieves the cart for a specific user with all items"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(convertToCartResponse(cart));
    }

    @Operation(
            summary = "Add product to cart",
            description = "Adds a product with specified quantity to the cart"
    )
    @PostMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<CartResponse> addProduct(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "Quantity to add") @RequestParam Integer quantity) {
        Cart cart = cartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok(convertToCartResponse(cart));
    }

    @Operation(
            summary = "Update cart item",
            description = "Updates the quantity of a product in the cart"
    )
    @PutMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<CartResponse> updateProduct(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "New quantity") @RequestParam Integer quantity) {
        Cart cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(convertToCartResponse(cart));
    }

    @Operation(
            summary = "Remove product from cart",
            description = "Removes a specific product from the cart"
    )
    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<Void> removeProduct(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Clear cart",
            description = "Removes all items from the user's cart"
    )
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    private CartResponse convertToCartResponse(Cart cart) {

        // Convert items
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        // Calculate total
        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Return response
        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                items.size(),
                totalPrice,
                cart.getCreatedAt(),
                null
        );
    }
}