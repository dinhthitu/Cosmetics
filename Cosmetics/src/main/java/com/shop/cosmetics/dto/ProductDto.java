package com.shop.cosmetics.dto;

import com.shop.cosmetics.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    Long productId;
    String productName;
    String productCode;
    String description;
    double basePrice;
    int stockQuantity;
    List<String> images;
    double averageRating;
    Category category;
}
