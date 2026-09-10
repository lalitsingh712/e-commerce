package com.lalit.e_commerce.controller;

import com.lalit.e_commerce.dto.request.ReviewRequest;
import com.lalit.e_commerce.dto.response.ReviewResponse;
import com.lalit.e_commerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name="Reviews",description = "Product review and rating management APIs")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/product/{productId}/user/{userId}")
    @Operation(
            summary = "Add review to product",
            description = "Add a new review and rating (1-5 starts) to a product. One review per user per product. ")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReviewResponse> addReview(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody ReviewRequest request){

        ReviewResponse response=reviewService.addReview(productId,userId,request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    @Operation(
            summary = "Get product reviews",
            description = "Retrieve all reviews for a specific product, sorted by latest first"
    )
    public ResponseEntity<List<ReviewResponse>> getProductReviews(
            @Parameter(description = "Product ID") @PathVariable Long productId
    ){
        List<ReviewResponse> reviews=reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviews);
    }


    @DeleteMapping("/{reviewId}/user/{userId}")
    @Operation(
            summary = "Delete review",
            description = "Delete a review (only the review owner can delete their review)"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID") @PathVariable Long reviewId,
            @Parameter(description = "User ID") @PathVariable Long userId
            ){

        reviewService.deleteReview(reviewId,userId);

        return ResponseEntity.noContent().build();
    }
}
