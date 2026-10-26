package com.gopi.airbnb.dto.response;

import com.gopi.airbnb.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(String transaction_id, Long booking_id, PaymentStatus payment_status, String message,
                              BigDecimal amount) {
}
