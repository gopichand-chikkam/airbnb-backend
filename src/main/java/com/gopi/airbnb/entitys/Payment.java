package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String transactionId;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(mappedBy = "payment")
    private Booking booking;

}
