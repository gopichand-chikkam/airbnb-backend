package com.gopi.airbnb.entitys;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BookingGuest {
    private Long id;
    private Long bookingId;
    private Long guestId;
}
