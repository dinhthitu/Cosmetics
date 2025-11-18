package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.CartDto;
import com.shop.cosmetics.dto.CartItemDto;
import com.shop.cosmetics.entity.Cart;
import com.shop.cosmetics.entity.CartItem;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/carts")

public class CartController {

    CartService cartService;

//    @PostMapping("/new")
//    public ApiResponse<Cart> createCart(){
//        return ApiResponse.<Cart>builder()
//                .result(cartService.createCart())
//                .build();
//    }

    @PostMapping("/add/{id}")
    public ApiResponse<CartDto> addItemToCart(@PathVariable Long id, @RequestParam Long productId, @RequestParam int quantity){
        return ApiResponse.<CartDto>builder()
                .result(cartService.addItemToCart(id, productId, quantity))
                .build();
    }

    @PatchMapping("/update/{id}")
    public ApiResponse<CartDto> updateCart( @PathVariable Long id, @RequestParam int quantity){
        return ApiResponse.<CartDto>builder()
                .result(cartService.updateCart(id, quantity))
                .build();
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse<CartDto> removeItem(@PathVariable Long id){
        return ApiResponse.<CartDto>builder()
                .result(cartService.removeItem(id))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteCart(){
        cartService.deleteCart();
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CartItemDto>> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "cartItemId") String sortBy){
        return ApiResponse.<List<CartItemDto>>builder()
                .result(cartService.getAll(pageNo, pageSize, sortBy))
                .build();
    }
}
