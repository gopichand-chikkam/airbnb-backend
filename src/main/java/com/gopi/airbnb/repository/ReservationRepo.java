package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation,Long> {
    List<Reservation> findByPaymentWindowExpiresBefore(LocalDateTime now);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reservation r where r.id = :id")
    Optional<Reservation> findByIdForUpdate(Long id);
}
