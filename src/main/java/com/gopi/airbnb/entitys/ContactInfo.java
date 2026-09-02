package com.gopi.airbnb.entitys;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ContactInfo {
    private Long id;
    private String completeAddress;
    private String location;
    private String email;
    private String phoneNumber;

}
