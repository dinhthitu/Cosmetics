package com.shop.cosmetics.repository;

import com.shop.cosmetics.entity.Cart;
import com.shop.cosmetics.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByCart(Cart cart, Pageable paging);

    @Query("select ci from CartItem ci where ci.cart = :cart and ci.product.productId = :productId")
    CartItem findByCartAndProductId(@Param("cart") Cart cart, @Param("productId") Long productId);
}
