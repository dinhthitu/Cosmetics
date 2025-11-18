package com.shop.cosmetics.controller;

import com.shop.cosmetics.dto.CheckoutRequest;
import com.shop.cosmetics.dto.OrderDto;
import com.shop.cosmetics.entity.Order;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class OrderController {
    OrderService orderService;

    // error in local date time ?  1-2-4

    @PostMapping("/checkout")
    public ApiResponse<OrderDto> createOrder (@RequestBody CheckoutRequest request){
        return ApiResponse.<OrderDto>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ApiResponse.<OrderDto>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    @PatchMapping("/cancel/{orderId}")
    public ApiResponse<Void> cancelOrder (@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<OrderDto>> getAll(){
        return ApiResponse.<List<OrderDto>>builder()
                .result(orderService.getAll())
                .build();
    }

}
