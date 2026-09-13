package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    List<Inventory> findByRoomId(Long roomId);

    List<Inventory> findByRoomIdAndDateGreaterThanEqualOrderByDateAsc(Long id, LocalDate now);
}
