package com.shop.cosmetics.service;

import com.shop.cosmetics.Utils.Helpers;
import com.shop.cosmetics.dto.CheckoutRequest;
import com.shop.cosmetics.dto.OrderDto;
import com.shop.cosmetics.entity.*;
import com.shop.cosmetics.enums.PaymentMethod;
import com.shop.cosmetics.enums.Status;
import com.shop.cosmetics.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j  // Thay System.out bằng log chuẩn
public class OrderService {

    CartRepository cartRepository;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    ModelMapper modelMapper;

    @Transactional
    public OrderDto createOrder(CheckoutRequest request) {
        User currentUser = Helpers.getCurrentUser();
        log.info("Checkout started for user: {} ({})", currentUser.getUsername(), currentUser.getUserId());

        Cart cart = cartRepository.findUserById(currentUser.getUserId());
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Empty cart");
        }

        String address = isEmpty(request.getAddress()) ? currentUser.getAddress() : request.getAddress();
        String username = isEmpty(request.getUsername()) ? currentUser.getUsername() : request.getUsername();
        String phoneNumber = isEmpty(request.getPhoneNumber()) ? currentUser.getPhoneNumber() : request.getPhoneNumber();

        Order order = Order.builder()
                .user(currentUser)
                .method(request.getMethod())
                .address(address)
                .totalPrice(cart.getTotalPrice())
                .status(Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        log.info("Order created: ID = {}", order.getOrderId());

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
        log.info("Cart cleared for user: {}", currentUser.getUserId());

        Payment payment = Payment.builder()
                .order(order)
                .totalPrice(order.getTotalPrice())
                .method(request.getMethod())
                .status(request.getMethod() == PaymentMethod.COD ? Status.PAID : Status.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        order.setPayment(payment);
        paymentRepository.save(payment);
        log.info("Payment created: ID = {}, Status = {}", payment.getPaymentId(), payment.getStatus());

        return modelMapper.map(order, OrderDto.class);
    }

    public List<OrderDto> getAll() {
        User user = Helpers.getCurrentUser();
        return orderRepository.findByUser(user).stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        User user = Helpers.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == Status.SHIPPING || order.getStatus() == Status.SUCCESS) {
            throw new RuntimeException("Your order successes. You can not cancel");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);

        Payment payment = order.getPayment();
        if (payment != null) {
            payment.setStatus(Status.CANCELLED);
            paymentRepository.save(payment);
        }

        log.info("Order {} cancelled by user {}", orderId, user.getUserId());
    }

    @Transactional
    public OrderDto getOrderById(Long orderId) {
        User user = Helpers.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderDto.class);
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}