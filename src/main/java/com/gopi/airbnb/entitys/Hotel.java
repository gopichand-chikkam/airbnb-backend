package com.gopi.airbnb.entitys;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Hotel {
    private Long id;
    private String city;
    private List<String> photos;
    private List<String> amenities;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    @OneToOne
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;

}
