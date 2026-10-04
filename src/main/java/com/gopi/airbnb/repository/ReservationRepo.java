package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation,Long> {
    List<Reservation> findByPaymentWindowExpiresBefore(LocalDateTime now);
}
