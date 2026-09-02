package com.gopi.airbnb.entitys;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Room {

    private Long id;
    private Long hotelId;
    private String type;
    private Double basePrice;
    private Date createdAt;
    private Date updatedAt;
    private List<String> photos;
    private List<String> amenities;
    private Integer totalCount;
    private Integer capacity;
}
