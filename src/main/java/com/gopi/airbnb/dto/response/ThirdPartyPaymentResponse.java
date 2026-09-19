package com.gopi.airbnb.dto.response;

import com.gopi.airbnb.enums.PaymentStatus;

public record ThirdPartyPaymentResponse(String transaction_id, PaymentStatus paymentStatus) {
}
