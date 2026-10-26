package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.InternalPayment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternalPaymentRepo extends JpaRepository<InternalPayment,Long> {



    InternalPayment findTopByReservationIdOrderByCreatedAtDesc(Long id);
}
