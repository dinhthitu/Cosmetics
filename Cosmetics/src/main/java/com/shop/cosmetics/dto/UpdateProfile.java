package com.shop.cosmetics.dto;

import com.shop.cosmetics.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UpdateProfile {
    String email;
    String username;
    String phoneNumber;
    Gender gender;
    String address;
}
