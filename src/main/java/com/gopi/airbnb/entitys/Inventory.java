package com.gopi.airbnb.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate date;
    private Integer bookedCount;
    private Integer totalCount ;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Double surgeFactor;
    private Boolean closed;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

//    @ManyToOne
//    @JoinColumn(name = "hotel_id")
//    private Hotel hotel;




}
