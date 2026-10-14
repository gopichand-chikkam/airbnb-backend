package com.gopi.airbnb.dto.response;

import com.gopi.airbnb.enums.PaymentStatus;

import java.math.BigDecimal;

public record ThirdPartyResponse(PaymentStatus paymentStatus, String transactionId, Long reservationId, BigDecimal amount) {
}
