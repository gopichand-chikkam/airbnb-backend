package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    List<Inventory> findByRoomId(Long roomId);

    List<Inventory> findByRoomIdAndDateGreaterThanEqualOrderByDateAsc(Long id, LocalDate now);


    List<Inventory> findByRoomIdAndDateBetween(Long id, LocalDate checkIn, LocalDate checkOut);
    List<Inventory> findByRoomIdAndDateGreaterThanEqualAndDateLessThan(Long id, LocalDate checkIn, LocalDate checkOut);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT i
    FROM Inventory i
    WHERE i.room.id = :roomId
      AND i.date >= :checkIn
      AND i.date < :checkOut
""")
    List<Inventory> findByRoomIdAndDateGreaterThanEqualAndDateLessThanForUpdate(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
