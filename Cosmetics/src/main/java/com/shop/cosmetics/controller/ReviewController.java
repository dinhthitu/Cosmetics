package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.ReviewDto;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/product/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<ReviewDto> createReview(
            @PathVariable Long productId,
            @RequestBody ReviewDto reviewDto) {
        return ApiResponse.<ReviewDto>builder()
                .result(reviewService.createReview(reviewDto, productId))
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<ReviewDto>> getReviewsByProduct(@PathVariable Long productId) {
        return ApiResponse.<List<ReviewDto>>builder()
                .result(reviewService.getAll(productId))
                .build();
    }

    @GetMapping("/product/{productId}/rating")
    public ApiResponse<Double> getAverageRating(@PathVariable Long productId) {
        return ApiResponse.<Double>builder()
                .result(reviewService.getAverageRating(productId))
                .build();
    }
}
