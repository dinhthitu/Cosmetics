package com.shop.cosmetics.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.management.relation.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    Long userId;
    String email;
    String username;
    Role role;
}
