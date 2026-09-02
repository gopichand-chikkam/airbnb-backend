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
public class Booking {
    private Long id;
    private Long hotelId;
    private Long roomId;
    private Long userId;
    private Date CreatedAt;
    private Date updatedAt;
    private PaymentStatus bookingStatus;
    private Date checkInDate;
    private Date checkOutDate;
    private Long paymentId;


}
