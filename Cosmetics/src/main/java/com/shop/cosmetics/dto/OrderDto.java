package com.shop.cosmetics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shop.cosmetics.entity.OrderItem;
import com.shop.cosmetics.enums.PaymentMethod;
import com.shop.cosmetics.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    Long orderId;
    PaymentMethod method;
    Status status;
    double totalPrice;
    List<OrderItem> orderItems;
}
