package com.shop.cosmetics.repository;

import com.shop.cosmetics.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select avg(r.rating) from Review  r where r.product.productId = :productId")
    double calculateAverageRating(@Param("productId") Long productId);
}
