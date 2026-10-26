package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.PaymentService;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/payment")
public class PaymentWebHookController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public void paymentWebHook(@RequestBody ThirdPartyResponse thirdPartyResponse){
        paymentService.handlingPaymentResponse(thirdPartyResponse);
    }




}
