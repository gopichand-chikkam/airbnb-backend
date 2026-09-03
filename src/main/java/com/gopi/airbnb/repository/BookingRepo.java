package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepo extends JpaRepository<Booking,Long> {
}
