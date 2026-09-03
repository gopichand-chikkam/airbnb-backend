package com.gopi.airbnb.entitys;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String city;

    @ElementCollection
    private List<String> photos;
    @ElementCollection
    private List<String> amenities;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    @OneToOne
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contact_info;
    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;
    @OneToMany(mappedBy = "hotel")
    private List<Inventory> inventoryList;

    @OneToMany(mappedBy = "hotel")
    private List<Booking> bookings;


}
