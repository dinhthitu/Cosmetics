package com.shop.cosmetics.dto;

import com.shop.cosmetics.annotation.PasswordValidation;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Register {
    String email;
    @Column(nullable = false)
    String username;
    @PasswordValidation
    String password;
}
