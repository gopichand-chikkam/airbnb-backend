package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.PaymentStatus;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Payment {

    private Long id;
    private String transactionId;
    private Double price;
    private Date createdAt;
    private Date updatedAt;
    private PaymentStatus paymentStatus;

}
