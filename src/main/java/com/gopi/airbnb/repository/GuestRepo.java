package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepo extends JpaRepository<Guest,Long> {
}
