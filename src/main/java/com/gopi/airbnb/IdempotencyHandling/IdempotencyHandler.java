package com.gopi.airbnb.IdempotencyHandling;


import com.gopi.airbnb.dto.functionalDto.PreparePaymentResult;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;
import com.gopi.airbnb.entitys.IdempotencyToPaymentResponse;
import com.gopi.airbnb.entitys.InternalPayment;
import com.gopi.airbnb.entitys.Reservation;
import com.gopi.airbnb.enums.PaymentStatus;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.IdempotencyToPaymentResponseRepo;

import com.gopi.airbnb.repository.InternalPaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyHandler {

    private final IdempotencyToPaymentResponseRepo idempotencyToPaymentResponseRepo;
    private final InternalPaymentRepo internalPaymentRepo;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String checkTheDuplicatePaymentResponse(ThirdPartyResponse thirdPartyResponse) {
        try {
            IdempotencyToPaymentResponse idempotency = IdempotencyToPaymentResponse.builder()
                    .paymentStatus(thirdPartyResponse.paymentStatus())
                    .providerPaymentId(thirdPartyResponse.transactionId())
                    .build();
            IdempotencyToPaymentResponse savedIdempotency = idempotencyToPaymentResponseRepo.save(idempotency);
            idempotencyToPaymentResponseRepo.flush();
            return "NewResponse";
        } catch (DataIntegrityViolationException k) {
            return "Duplicate";

        }catch (Exception e){
            String h= e.getMessage();
           return h;
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public PreparePaymentResult checkIfTheRequestIsDuplicateOrNot(Reservation reservation) {

        InternalPayment internalPayment = internalPaymentRepo.findTopByReservationIdOrderByCreatedAtDesc(reservation.getId());

        if(internalPayment==null)return new PreparePaymentResult(PaymentStatus.NOT_INITIATED,null) ;
        return new PreparePaymentResult(internalPayment.getPaymentStatus(),internalPayment);

    }


}
