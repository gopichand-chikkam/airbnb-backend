package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.ThirdPartyPaymentRequest;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;

public interface FakePaymentProvider {
    public ThirdPartyResponse createPayment(ThirdPartyPaymentRequest thirdPartyPaymentRequest);
}
