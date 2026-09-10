package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.ReviewRequest;
import com.lalit.e_commerce.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(Long productId, Long userId, ReviewRequest request);
    List<ReviewResponse> getProductReviews(Long productId);
    void deleteReview(Long reviewId, Long userId);
}
