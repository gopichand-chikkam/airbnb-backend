package com.gopi.airbnb.dto.response;

import com.gopi.airbnb.enums.PaymentStatus;

public record PaymentResponse(Long booking_id, PaymentStatus payment_status, String message) {
}
