package com.shop.cosmetics.service;

import com.shop.cosmetics.Utils.Helpers;
import com.shop.cosmetics.dto.CartDto;
import com.shop.cosmetics.dto.CartItemDto;
import com.shop.cosmetics.entity.*;
import com.shop.cosmetics.enums.ErrorCode;
import com.shop.cosmetics.exception.Exceptions;
import com.shop.cosmetics.repository.CartItemRepository;
import com.shop.cosmetics.repository.CartRepository;
import com.shop.cosmetics.repository.ProductRepository;
import com.shop.cosmetics.strategy.PricingService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    PricingService pricingService;
    ModelMapper modelMapper;

    @Transactional
    public Cart createCart(User user){
        Cart cart = cartRepository.findUserById(user.getUserId());
        if(cart != null){
            return cart;
        }else{
            cart = Cart.builder()
                    .user(user)
                    .totalPrice(0.0)
                    .build();
            return cartRepository.save(cart);
        }
    }

    @Transactional
    public CartDto addItemToCart(Long cartId, Long productId, int quantity){
        User user = Helpers.getCurrentUser();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Helpers.CheckCartPermission(user,cart);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exceptions(ErrorCode.PRODUCT_NOT_FOUND));
        if(product.getStockQuantity() < quantity){
            throw new RuntimeException("Out of stock");
        }

        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, productId);
//        CartItem cartItem = cart.getCartItems().stream()
//                .filter(items -> items.getProduct().getProductId().equals(productId))
//                .findFirst()
//                .orElse(null);
        if(cartItem != null){
            cartItem.setQuantity(quantity);
            cartItem.setPrice(pricingService.calculateDynamicPrice(product.getBasePrice() * quantity));
            cartItemRepository.save(cartItem);
        }else{
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .price(pricingService.calculateDynamicPrice(product.getBasePrice()) * quantity)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
//            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }
        totalPrice(cart);
        cartRepository.save(cart);
        return modelMapper.map(cart, CartDto.class);
    }

    @Transactional
    public CartDto updateCart(Long cartItemId, int quantity){
        User user = Helpers.getCurrentUser();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Can not find item in cart"));
        Cart cart = cartItem.getCart();
        Helpers.CheckCartPermission(user, cart);
        cartItem.setQuantity(quantity);
        if(cartItem.getQuantity() <= 0) {
            return removeItem(cartItemId);
        }

        cartItem.setPrice(pricingService.calculateDynamicPrice(cartItem.getProduct().getBasePrice()));
        cartItemRepository.save(cartItem);
        totalPrice(cart);
        cartRepository.save(cart);
        return modelMapper.map(cart, CartDto.class);
    }

    @Transactional
    public CartDto removeItem(Long cartItemId){
        User user = Helpers.getCurrentUser();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Can not find item"));
        Cart cart = cartItem.getCart();
        Helpers.CheckCartPermission(user, cart);
        Product product = cartItem.getProduct();
//        product.setStockQuantity(product.getStockQuantity() + cartItem.getQuantity());
        productRepository.save(product);
        cart.getCartItems().remove(cartItem);
        totalPrice(cart);
        cartRepository.save(cart); // Hibernate tự delete cartItem

        return modelMapper.map(cart, CartDto.class);
    }

    @Transactional
    public void deleteCart(){
        User user = Helpers.getCurrentUser();
        Cart cart = cartRepository.findUserById(user.getUserId());
        if(cart == null){
            throw new RuntimeException("cart not found");
        }
        Helpers.CheckCartPermission(user, cart);
//        for(CartItem item : cart.getCartItems()){
//            Product product = item.getProduct();
//            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
//            productRepository.save(product);
//        }
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }

    @Transactional()
    public List<CartItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy){
        User user = Helpers.getCurrentUser();
        Cart cart = cartRepository.findUserById(user.getUserId());

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        Helpers.CheckCartPermission(user, cart);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<CartItem> pagedResult = cartItemRepository.findByCart(cart, paging);

        return pagedResult.hasContent()
                ? pagedResult.getContent()
                .stream()
                .map(cartItem -> modelMapper.map(cartItem, CartItemDto.class))
                .toList()
                : new ArrayList<>();

    }


    void totalPrice(Cart cart){
        double price = cart.getCartItems().stream()
                        .mapToDouble(CartItem::getPrice)
                                .sum();
        cart.setTotalPrice(pricingService.calculatePaymentPrice(price));
    }
}
