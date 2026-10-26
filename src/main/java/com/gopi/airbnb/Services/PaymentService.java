package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.response.PaymentResponse;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;

public interface PaymentService {


    PaymentResponse initiatePayment(PaymentRequest paymentRequest);
    PaymentResponse handlingPaymentResponse(ThirdPartyResponse thirdPartyResponse);
}
