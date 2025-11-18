package com.shop.cosmetics.repository;

import com.shop.cosmetics.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p.basePrice from Product p where p.productId = :productId")
    double findNewBasePrice(@Param("productId")Long productId);
}
