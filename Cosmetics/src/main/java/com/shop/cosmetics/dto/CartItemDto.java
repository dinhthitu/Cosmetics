package com.shop.cosmetics.dto;

import com.shop.cosmetics.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CartItemDto {
    long cartItemId;
    Product product;
    int quantity;
    double price;

}
