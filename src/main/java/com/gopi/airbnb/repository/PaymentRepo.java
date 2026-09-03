package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
