package com.gopi.airbnb.entitys;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;
}
