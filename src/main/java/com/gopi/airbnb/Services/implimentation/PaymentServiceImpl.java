package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.IdempotencyHandling.IdempotencyHandler;
import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.Services.FakePaymentProvider;
import com.gopi.airbnb.Services.PaymentService;
import com.gopi.airbnb.Services.PreparePaymentService;
import com.gopi.airbnb.dto.functionalDto.PreparePaymentResult;
import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.requests.ThirdPartyPaymentRequest;
import com.gopi.airbnb.dto.response.PaymentResponse;

import com.gopi.airbnb.dto.response.ThirdPartyResponse;
import com.gopi.airbnb.entitys.*;
import com.gopi.airbnb.enums.BookingStatus;
import com.gopi.airbnb.enums.PaymentStatus;
import com.gopi.airbnb.enums.ReservationStatus;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ReservationRepo reservationRepo;
    private final InventoryRepo inventoryRepo;
    private final PaymentRepo paymentRepo;
    private final ReservationGuestRepo reservationGuestRepo;
    private final BookingGuestRepo bookingGuestRepo;
    private final BookingService bookingService;
    private final IdempotencyHandler idempotencyHandler;
    private final InternalPaymentRepo internalPaymentRepo;
    private final PreparePaymentService preparePaymentService;
    private final FakePaymentProvider fakePaymentProvider;
    private final BookingRepo bookingRepo;


    @Override
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        System.out.println("entered initated payment");
        PreparePaymentResult preparePaymentResult = preparePaymentService.prepareInternalPayment(paymentRequest);
        InternalPayment savedInternalPayment = preparePaymentResult.internalPayment();
        if (preparePaymentResult.paymentStatus() == PaymentStatus.FAILED || preparePaymentResult.paymentStatus() == PaymentStatus.NOT_INITIATED) {
            InternalPayment internalPayment = preparePaymentResult.internalPayment();
            Reservation reservation = internalPayment.getReservation();
            User user = reservation.getUser();

            ThirdPartyPaymentRequest thirdPartyPaymentRequest = new ThirdPartyPaymentRequest(internalPayment.getId(), internalPayment.getAmount(), user.getId(), user.getEmail());
            ThirdPartyResponse thirdPartyResponse = createThirdPartyConnectionForPayment(thirdPartyPaymentRequest);

            System.out.println(thirdPartyResponse.transactionId() + "--" + thirdPartyResponse.paymentStatus());
            System.out.println("-------------------------");
            PaymentResponse paymentResponse = handlingPaymentResponse(thirdPartyResponse);
            return paymentResponse;
        }
        return new PaymentResponse(savedInternalPayment.getProviderPaymentOrderId(), null, savedInternalPayment.getPaymentStatus(), "your payment is in pending satte", savedInternalPayment.getAmount());
    }

    //Todo change return function
    private ThirdPartyResponse createThirdPartyConnectionForPayment(ThirdPartyPaymentRequest thirdPartyPaymentRequest) {
        return fakePaymentProvider.createPayment(thirdPartyPaymentRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse handlingPaymentResponse(ThirdPartyResponse thirdPartyResponse) {
        if (thirdPartyResponse == null) throw new InvalidDetailsException("thirdPartyResponse is null");
        String exception= idempotencyHandler.checkTheDuplicatePaymentResponse(thirdPartyResponse);
         System.out.println(exception);
        if (exception.equals("NewResponse")) {

            PaymentStatus status = thirdPartyResponse.paymentStatus();
            InternalPayment internalPayment = internalPaymentRepo.findById(thirdPartyResponse.merchantOrderId()).orElseThrow(() -> new ResourceNotFoundException("Internal Payment Not Found"));
            internalPayment.setPaymentStatus(thirdPartyResponse.paymentStatus());
            internalPayment.setProviderPaymentOrderId(thirdPartyResponse.transactionId());
            internalPayment.setUpdatedAt(LocalDateTime.now());
              InternalPayment savedPayment1= internalPaymentRepo.save(internalPayment);
            Reservation reservation = internalPayment.getReservation();

            if (status == PaymentStatus.SUCCESS) {
                if (reservation.getReservationStatus() == ReservationStatus.ACTIVE) {
                    bookingService.updateReservationAndInventoryByReservationStatus(reservation, false);
                    reservation.setReservationStatus(ReservationStatus.CONFIRMED);
                    Payment successPayment = Payment.builder()
                            .paymentStatus(PaymentStatus.SUCCESS)
                            .providerPaymentId(thirdPartyResponse.transactionId())
                            .price(reservation.getAmount())
                            .reservation(reservation)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    Payment savedPayment = paymentRepo.save(successPayment);
                    Booking successBooking = Booking.builder()
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .bookingStatus(BookingStatus.CONFIRMED)
                            .checkInDate(reservation.getCheck_in())
                            .checkOutDate(reservation.getCheck_out())
                            .hotel(reservation.getRoom().getHotel())
                            .room(reservation.getRoom())
                            .user(reservation.getUser())
                            .reservation(reservation)
                            .build();
                    Booking savedBooking = bookingRepo.save(successBooking);
                    List<ReservationGuest> guestList = reservationGuestRepo.findByReservationId(reservation.getId());

                    for (ReservationGuest reservationGuest : guestList) {
                        BookingGuest bookingGuest = BookingGuest.builder()
                                .booking(savedBooking)
                                .guest(reservationGuest.getGuest())
                                .build();
                        bookingGuestRepo.save(bookingGuest);
                    }

                    return new PaymentResponse(thirdPartyResponse.transactionId(), savedBooking.getId(), thirdPartyResponse.paymentStatus(), "Your Booking has done success fully", reservation.getAmount());
                } else if (reservation.getReservationStatus() == ReservationStatus.EXPIRED) {
                   String check_in= reservation.getCheck_in().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    String check_out= reservation.getCheck_out().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    if (bookingService.startBooking(new BookingRequest(null, reservation.getRoom().getId(), null, reservation.getTotalGuest(), check_in, check_out)).isBookingPossible()) {
                        bookingService.updateReservationAndInventoryByReservationStatus(reservation, true);
                        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
                        Payment successPayment = Payment.builder()
                                .paymentStatus(PaymentStatus.SUCCESS)
                                .providerPaymentId(thirdPartyResponse.transactionId())
                                .price(reservation.getAmount())
                                .reservation(reservation)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                        Payment savedPayment = paymentRepo.save(successPayment);
                        Booking successBooking = Booking.builder()
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .bookingStatus(BookingStatus.CONFIRMED)
                                .checkInDate(reservation.getCheck_in())
                                .checkOutDate(reservation.getCheck_out())
                                .hotel(reservation.getRoom().getHotel())
                                .room(reservation.getRoom())
                                .user(reservation.getUser())
                                .reservation(reservation)
                                .build();
                        Booking savedBooking = bookingRepo.save(successBooking);
                        List<ReservationGuest> guestList = reservationGuestRepo.findByReservationId(reservation.getId());

                        for (ReservationGuest reservationGuest : guestList) {
                            reservationGuest.setReservationStatus(ReservationStatus.CONFIRMED);
                            BookingGuest bookingGuest = BookingGuest.builder()
                                    .booking(savedBooking)
                                    .guest(reservationGuest.getGuest())
                                    .build();
                            bookingGuestRepo.save(bookingGuest);
                        }
                        return new PaymentResponse(thirdPartyResponse.transactionId(), successBooking.getId(), thirdPartyResponse.paymentStatus(), "Your Booking has done success fully", reservation.getAmount());
                    } else {
                        //  Todo  when room not available initiate refund to coustomer
                    }

                }


            } else if (status == PaymentStatus.FAILED) { // if fail i am creating payment entity to maintain a record
                Payment successPayment = Payment.builder()
                        .paymentStatus(PaymentStatus.FAILED)
                        .providerPaymentId(thirdPartyResponse.transactionId())
                        .price(reservation.getAmount())
                        .reservation(reservation)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Payment savedPayment = paymentRepo.save(successPayment);
            }

        }
        return new PaymentResponse(thirdPartyResponse.transactionId(), null, thirdPartyResponse.paymentStatus(), null, null);


    }


}

