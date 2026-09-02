package com.gopi.airbnb.entitys;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Inventory {
    private Long id;
    private Long hotelId;
    private Long roomId;
    private Date date;
    private Integer bookedCount;
    private Integer totalCount ;
    private Date CreatedAt;
    private Date updatedAt;
    private Double surgeFactor;
    private Boolean closed;



}
