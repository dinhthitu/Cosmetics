package com.shop.cosmetics.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;
    String productName;
    String productCode;
    String description;
    @Column(name = "price")
    double basePrice;
    int stockQuantity;
    @ElementCollection(fetch = FetchType.LAZY)
    List<String> images;
    double averageRating;
    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    Category category;

}
