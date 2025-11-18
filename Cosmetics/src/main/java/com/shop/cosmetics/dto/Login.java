package com.shop.cosmetics.dto;

import com.shop.cosmetics.annotation.PasswordValidation;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login {
    String email;
    @PasswordValidation
    String password;
}
