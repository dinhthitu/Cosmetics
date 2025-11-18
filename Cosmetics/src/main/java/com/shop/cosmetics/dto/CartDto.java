package com.shop.cosmetics.dto;

import com.shop.cosmetics.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    long cartId;
    double totalPrice;
    List<CartItemDto> cartItems;
}
