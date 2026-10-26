package com.gopi.airbnb.dto.functionalDto;

import com.gopi.airbnb.entitys.InternalPayment;
import com.gopi.airbnb.enums.PaymentStatus;

public record PreparePaymentResult(PaymentStatus paymentStatus, InternalPayment internalPayment) {
}
