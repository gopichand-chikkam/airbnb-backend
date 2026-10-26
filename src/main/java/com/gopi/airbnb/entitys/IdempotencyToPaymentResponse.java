package com.gopi.airbnb.entitys;


import com.gopi.airbnb.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "IdempotencyToPaymentResponse", uniqueConstraints = @UniqueConstraint(name = "unique_status_transactionId", columnNames = {"providerPaymentId", "paymentStatus"}))
public class IdempotencyToPaymentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String providerPaymentId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
