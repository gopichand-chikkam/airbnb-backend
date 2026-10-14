package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.BookingGuestService;
import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.Services.GuestService;
import com.gopi.airbnb.Services.PaymentService;
import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.requests.GuestRequest;
import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.requests.ThirdPartyPaymentRequest;
import com.gopi.airbnb.dto.response.PaymentResponse;

import com.gopi.airbnb.dto.response.ThirdPartyPaymentResponse;
import com.gopi.airbnb.dto.response.ThirdPartyResponse;
import com.gopi.airbnb.entitys.*;
import com.gopi.airbnb.enums.BookingStatus;
import com.gopi.airbnb.enums.Gender;
import com.gopi.airbnb.enums.PaymentStatus;
import com.gopi.airbnb.enums.ReservationStatus;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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


    @Transactional(rollbackFor = Exception.class)
// not require for this function but this function is depend on update reservation . so that why i have written
    public boolean handlingPaymentResponse(ThirdPartyResponse thirdPartyResponse) {
        if (thirdPartyResponse == null) throw new InvalidDetailsException("thirdPartyResponse is null");
        PaymentStatus status = thirdPartyResponse.paymentStatus();
        Reservation reservation = reservationRepo.findById(thirdPartyResponse.reservationId()).orElseThrow(() -> new ResourceNotFoundException("Reservation Not Found"));
        if (status == PaymentStatus.SUCCESS) {
            if (reservation.getReservationStatus() == ReservationStatus.ACTIVE) {
                updateReservationAndInventoryByReservationStatus(reservation, false);
                reservation.setReservationStatus(ReservationStatus.EXPIRED);
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
                List<ReservationGuest> guestList = reservationGuestRepo.findByReservationId(reservation.getId());

                for (ReservationGuest reservationGuest : guestList) {
                    BookingGuest bookingGuest = BookingGuest.builder()
                            .booking(successBooking)
                            .guest(reservationGuest.getGuest())
                            .build();
                    bookingGuestRepo.save(bookingGuest);
                }
                return true;

            } else if (reservation.getReservationStatus() == ReservationStatus.EXPIRED) {

                if (bookingService.startBooking(new BookingRequest(null, reservation.getRoom().getId(), null, reservation.getTotalGuest(), reservation.getCheck_in().toString(), reservation.getCheck_out().toString())).isBookingPossible()) {
                    updateReservationAndInventoryByReservationStatus(reservation, true);
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
                    List<ReservationGuest> guestList = reservationGuestRepo.findByReservationId(reservation.getId());

                    for (ReservationGuest reservationGuest : guestList) {
                        BookingGuest bookingGuest = BookingGuest.builder()
                                .booking(successBooking)
                                .guest(reservationGuest.getGuest())
                                .build();
                        bookingGuestRepo.save(bookingGuest);
                    }
                    return true;
                } else {
                    //  Todo  when room not available initiate refund to coustomer
                }

            }

        } else { // if fail i am creating payment entity to maintain a record
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
        return false;
//
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateReservationAndInventoryByReservationStatus(Reservation reservation, boolean isReservationExpired) {

        List<Inventory> inventoryList = inventoryRepo.findByRoomIdAndDateGreaterThanEqualAndDateLessThanForUpdate(reservation.getRoom().getId(), reservation.getCheck_in(), reservation.getCheck_out());
        int guestCount = reservation.getTotalGuest();


        int roomsRequired = (int) Math.ceil(guestCount / (double) reservation.getRoom().getCapacity());
        System.out.println(roomsRequired + "--------------------");

        for (Inventory inventory : inventoryList) {
            if (!isReservationExpired) {
                inventory.setHoldCount(inventory.getHoldCount() - roomsRequired);
                inventory.setBookedCount(inventory.getBookedCount() + roomsRequired);
                inventoryRepo.save(inventory);
            } else {
                inventory.setHoldCount(inventory.getTotalCount() - roomsRequired);
                inventory.setTotalCount(inventory.getBookedCount() + roomsRequired);
                inventoryRepo.save(inventory);
            }

        }

    }


}
