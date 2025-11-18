package com.shop.cosmetics.entity;

import com.shop.cosmetics.enums.PaymentMethod;
import com.shop.cosmetics.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long paymentId;

    @OneToOne
    @JoinColumn(name = "orderId")
    Order order;

    @Enumerated(EnumType.STRING)
    PaymentMethod method;
    @Enumerated(EnumType.STRING)
    Status status;
    String transactionId;
    double totalPrice;
    @CreationTimestamp
    LocalDateTime paymentDate;

}
