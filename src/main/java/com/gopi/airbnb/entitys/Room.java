package com.gopi.airbnb.entitys;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String type;
    private Double basePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ElementCollection
    private List<String> photos;
    @ElementCollection
    private List<String> amenities;
    private Integer totalCount;
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @OneToMany(mappedBy = "room")
    private List<Inventory> roomInventory;
    @OneToMany(mappedBy = "room")
    private List<Booking>bookings;

}
