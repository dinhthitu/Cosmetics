package com.shop.cosmetics.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
}
