package com.shop.cosmetics.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long cartItemId;
    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    Cart cart;
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    Product product;
    int quantity;
    double price;
}
