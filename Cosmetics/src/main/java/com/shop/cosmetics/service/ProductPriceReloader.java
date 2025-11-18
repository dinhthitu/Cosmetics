package com.shop.cosmetics.service;

import com.shop.cosmetics.entity.Cart;
import com.shop.cosmetics.entity.CartItem;
import com.shop.cosmetics.entity.Product;
import com.shop.cosmetics.repository.CartItemRepository;
import com.shop.cosmetics.repository.CartRepository;
import com.shop.cosmetics.repository.ProductRepository;
import com.shop.cosmetics.strategy.PricingService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductPriceReloader {
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    PricingService pricingService;

    @Transactional
    @Scheduled(fixedRate = 30*60*1000)
    public void reloadProductPrices(){
        List<Product> products = productRepository.findAll();
        for(Product product: products){
            double newBasePrice = productRepository.findNewBasePrice(product.getProductId());
            if(newBasePrice != product.getBasePrice()){
                product.setBasePrice(pricingService.calculateDynamicPrice(newBasePrice));
                productRepository.save(product);
            }

        }

        List<CartItem> cartItems = cartItemRepository.findAll();
        for(CartItem cartItem: cartItems){
            Product product = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            double newPrice = product.getBasePrice() * cartItem.getQuantity();
            if(newPrice != cartItem.getPrice()){
                cartItem.setPrice(newPrice);
                cartItemRepository.save(cartItem);
            }
        }

        List<Cart> carts = cartRepository.findAll();
        for(Cart cart: carts){
            double newTotalPrice = cart.getCartItems().stream()
                    .mapToDouble(CartItem::getPrice)
                    .sum();
            if(newTotalPrice != cart.getTotalPrice()){
                cart.setTotalPrice(newTotalPrice);
                cartRepository.save(cart);
            }
        }
    }

}
