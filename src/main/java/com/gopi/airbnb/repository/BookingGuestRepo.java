package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.BookingGuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingGuestRepo extends JpaRepository<BookingGuest, Long> {
}
