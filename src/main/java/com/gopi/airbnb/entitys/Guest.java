package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Guest {

    private Long id;
    private Long userId;
    private String name;
    private Date createdAt;
    private Gender gender;

}
