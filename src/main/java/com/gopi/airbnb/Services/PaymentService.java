package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse initializePayment(PaymentRequest paymentRequest);
}
