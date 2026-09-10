package com.lalit.e_commerce.service.impl;

import com.lalit.e_commerce.dto.request.ReviewRequest;
import com.lalit.e_commerce.dto.response.ReviewResponse;
import com.lalit.e_commerce.entity.Product;
import com.lalit.e_commerce.entity.Review;
import com.lalit.e_commerce.entity.User;
import com.lalit.e_commerce.exception.BadRequestException;
import com.lalit.e_commerce.exception.ResourceNotFoundException;
import com.lalit.e_commerce.repository.ProductRepository;
import com.lalit.e_commerce.repository.ReviewRepository;
import com.lalit.e_commerce.repository.UserRepository;
import com.lalit.e_commerce.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private  ReviewRepository reviewRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional
    public ReviewResponse addReview(Long productId, Long userId, ReviewRequest request) {

        log.info("Adding review for product: {} by user: {}",productId,userId);

        //validate product exists
        Product product=productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));

        //validate user exists
        User user=userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if user already reviewed this product
        if(reviewRepo.findByProductIdAndUserId(productId,userId).isPresent()){
            throw new BadRequestException("You have already reviewed this product");
        }

        //validate ratting
        if (request.getRatting()<1 || request.getRatting()>5){
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        //create review
        Review review=new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(request.getRatting());
        review.setComment(request.getComment());

        Review savedReview =reviewRepo.save(review);

        updateProductRating(productId);

        log.info("Review added successfully with ID: {}",savedReview.getId());

        return convertToResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getProductReviews(Long productId) {
        productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ReviewResponse> reviews=reviewRepo.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        log.info("Retrieved {} reviews for product: {}",reviews.size(),productId);
        return reviews;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {

        log.info("Deleting review: {} by user: {}",reviewId,userId);

        Review review=reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Check if user owns the review
        if(!review.getUser().getId().equals(userId)){
            throw new BadRequestException("You can only delete your own reviews");
        }

        Long productId=review.getProduct().getId();
        reviewRepo.deleteById(reviewId);

        //update product rating
        updateProductRating(productId);

        log.info("Review {} deleted successfully", reviewId);
    }

    @Transactional
    private void updateProductRating(Long productId){
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Double avgRating = reviewRepo.getAverageRating(productId);

        Integer totalReviews=reviewRepo.getReviewCount(productId);

        product.setAverageRating(avgRating != null ? avgRating : 0.0);
        product.setTotalReviews(totalReviews != null ? totalReviews : 0);

        productRepo.save(product);

        log.debug("Product {} rating updated: avg={}, total={}", productId, avgRating, totalReviews);
    }


    private ReviewResponse convertToResponse(Review review){
        return new ReviewResponse(
                review.getId(),
                review.getProduct().getId(),
                review.getUser().getId(),
                review.getUser().getEmail(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
