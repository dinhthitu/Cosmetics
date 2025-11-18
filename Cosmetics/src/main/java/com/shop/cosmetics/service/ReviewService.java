package com.shop.cosmetics.service;

import com.shop.cosmetics.Utils.Helpers;
import com.shop.cosmetics.dto.ReviewDto;
import com.shop.cosmetics.entity.Product;
import com.shop.cosmetics.entity.Review;
import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.repository.ProductRepository;
import com.shop.cosmetics.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    ModelMapper modelMapper;
    ProductRepository productRepository;

    public ReviewDto createReview(ReviewDto request, Long productId){
        User user = Helpers.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Review review = modelMapper.map(request, Review.class);
        reviewRepository.save(review);
        return modelMapper.map(review, ReviewDto.class);
    }

    public List<ReviewDto> getAll(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));
        List<ReviewDto> reviewDtos = reviewRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
        return reviewDtos;
    }

    public double getAverageRating(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));
        Double averageRating = reviewRepository.calculateAverageRating(productId);
        return averageRating != null ? averageRating : 0.0;
    }
}
