package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room,Long> {
}
