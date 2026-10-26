package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String providerPaymentId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
