package com.shop.cosmetics.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reviewId;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    User user;
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    Product product;
    int rating;
    String comment;
    @CreationTimestamp
    LocalDateTime reviewDate;


}
