package com.shop.cosmetics.dto;

import com.shop.cosmetics.entity.Cart;
import com.shop.cosmetics.enums.Gender;
import com.shop.cosmetics.enums.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long userId;
    String username;
    String email;
    String googleId;
    String phoneNumber;
    Set<Roles>roles;
    Gender gender;
    String address;
    Cart cart;
}
