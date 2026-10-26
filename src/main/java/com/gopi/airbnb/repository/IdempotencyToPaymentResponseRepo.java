package com.gopi.airbnb.repository;

import com.gopi.airbnb.entitys.IdempotencyToPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyToPaymentResponseRepo extends JpaRepository<IdempotencyToPaymentResponse,Long> {
    boolean existsByProviderPaymentId(String s);
}
