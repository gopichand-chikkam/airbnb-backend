package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.FakePaymentProvider;
import com.gopi.airbnb.dto.requests.ThirdPartyPaymentRequest;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;
import com.gopi.airbnb.enums.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class FakePaymentProvideImpl implements FakePaymentProvider {
    @Override
    public ThirdPartyResponse createPayment(ThirdPartyPaymentRequest thirdPartyPaymentRequest) {
        return new ThirdPartyResponse(PaymentStatus.PENDING,"ugy6777y7",thirdPartyPaymentRequest.merchantOrderId(),thirdPartyPaymentRequest.amount(),"http/.gops sysyysetm");
    }
}
