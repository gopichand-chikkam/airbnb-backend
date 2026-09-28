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
@AllArgsConstructor
@Builder
@Table(name = "Room",
        uniqueConstraints = @UniqueConstraint(name = "unique_hotel_roomType", columnNames = {"hotel_id", "type"}))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Double basePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ElementCollection
    private List<String> photos;
    @ElementCollection
    private List<String> amenities;
    @Column(nullable = false)
    private Integer totalCount;
    @Column(nullable = false)
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> roomInventory;
    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

}
