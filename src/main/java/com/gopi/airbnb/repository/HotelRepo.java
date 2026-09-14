package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepo extends JpaRepository<Hotel,Long> {
    List<Hotel> findByCity(String city);
}
