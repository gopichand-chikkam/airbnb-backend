package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Guest;
import com.gopi.airbnb.entitys.ReservationGuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationGuestRepo extends JpaRepository<ReservationGuest,Long> {
    void deleteAllByReservationId(Long id);


    List<ReservationGuest> findByReservationId(Long id);
}
