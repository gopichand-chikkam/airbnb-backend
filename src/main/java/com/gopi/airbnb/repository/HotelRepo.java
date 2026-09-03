package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepo extends JpaRepository<Hotel,Long> {
}
