package com.shop.cosmetics.dto;

import com.shop.cosmetics.entity.User;
import com.shop.cosmetics.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutRequest {
    PaymentMethod method;
    String phoneNumber;
    String address;
    String username;

}
