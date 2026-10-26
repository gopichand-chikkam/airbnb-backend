package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.IdempotencyHandling.IdempotencyHandler;
import com.gopi.airbnb.Services.PreparePaymentService;
import com.gopi.airbnb.dto.functionalDto.PreparePaymentResult;
import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.entitys.InternalPayment;
import com.gopi.airbnb.entitys.Reservation;
import com.gopi.airbnb.entitys.User;
import com.gopi.airbnb.enums.PaymentStatus;
import com.gopi.airbnb.enums.ReservationStatus;
import com.gopi.airbnb.exceptions.BusinessException;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.InternalPaymentRepo;
import com.gopi.airbnb.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PreparePaymentServiceImpl implements PreparePaymentService {
    private final IdempotencyHandler idempotencyHandler;
    private final ReservationRepo reservationRepo;
    private final InternalPaymentRepo internalPaymentRepo;

    @Transactional(rollbackFor = Exception.class)
    public PreparePaymentResult prepareInternalPayment(PaymentRequest paymentRequest) {
        if (paymentRequest == null) throw new InvalidDetailsException("Payment Request Details are null");
        Reservation reservation = reservationRepo.findByIdForUpdate(paymentRequest.reservation_id()).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        User user = reservation.getUser();
        if (!Objects.equals(paymentRequest.user_id(), reservation.getUser().getId()))
            throw new InvalidDetailsException("Reservation User are Not matched");
        if (reservation.getPaymentWindowExpires().isBefore(LocalDateTime.now()))
            throw new BusinessException("Payment Reservation expires");
        if (reservation.getReservationStatus() == ReservationStatus.EXPIRED || reservation.getReservationStatus() == ReservationStatus.CONFIRMED)
            throw new BusinessException("Reservation Not Allowed");

        PreparePaymentResult preparePaymentResult = idempotencyHandler.checkIfTheRequestIsDuplicateOrNot(reservation);

         PaymentStatus paymentStatusFromIdempotency= preparePaymentResult.paymentStatus();
        if (paymentStatusFromIdempotency == PaymentStatus.FAILED || paymentStatusFromIdempotency == PaymentStatus.NOT_INITIATED) {
            //Creation of internal Payment Record
            InternalPayment internalPayment = InternalPayment.builder()
                    .paymentStatus(PaymentStatus.INITIATED)
                    .amount(reservation.getAmount())
                    .reservation(reservation)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            InternalPayment savedInternalPayment=internalPaymentRepo.save(internalPayment);
            return new PreparePaymentResult(PaymentStatus.NOT_INITIATED,savedInternalPayment);
        }
        return new PreparePaymentResult(paymentStatusFromIdempotency,preparePaymentResult.internalPayment());

    }

}

