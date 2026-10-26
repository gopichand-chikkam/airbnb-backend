package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.functionalDto.PreparePaymentResult;
import com.gopi.airbnb.dto.requests.PaymentRequest;


public interface PreparePaymentService {

    PreparePaymentResult prepareInternalPayment(PaymentRequest paymentRequest);
}
