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
@AllArgsConstructor
@Builder
@Table(
        name = "inventory",
        uniqueConstraints = @UniqueConstraint(name = "unique_inventory_room_date", columnNames = {"date", "room_id"})

)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Integer bookedCount;
    @Column(nullable = false)
    private Integer totalCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double surgeFactor;
    @Column(nullable = false)
    private Boolean closed;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "room_id",nullable = false)
    private Room room;

}
