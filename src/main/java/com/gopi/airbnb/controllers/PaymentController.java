package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.PaymentService;
import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/startPayment")
    ResponseEntity<PaymentResponse> initializePayment(@RequestBody PaymentRequest paymentRequest){
        PaymentResponse paymentResponse= paymentService.initializePayment(paymentRequest);
        return ResponseEntity.ok(paymentResponse);

    }

}
