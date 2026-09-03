package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date CreatedAt;
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "booking")
    private List<BookingGuest> bookingGuestList;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;


}
