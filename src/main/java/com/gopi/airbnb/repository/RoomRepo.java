package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room,Long> {
    List<Room> findByHotelId(Long hotelId);
}
