package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.BookingGuestService;
import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.Services.GuestService;
import com.gopi.airbnb.Services.PaymentService;
import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.requests.GuestRequest;
import com.gopi.airbnb.dto.requests.PaymentRequest;
import com.gopi.airbnb.dto.requests.ThirdPartyPaymentRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.dto.response.PaymentResponse;
import com.gopi.airbnb.dto.response.ThirdPartyPaymentResponse;
import com.gopi.airbnb.entitys.*;
import com.gopi.airbnb.enums.BookingStatus;
import com.gopi.airbnb.enums.Gender;
import com.gopi.airbnb.enums.PaymentStatus;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.PaymentRepo;
import com.gopi.airbnb.repository.RoomRepo;
import com.gopi.airbnb.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BookingService bookingService;
    private final GuestService guestService;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final PaymentRepo paymentRepo;
    private final BookingGuestService bookingGuestService;


    @Override
    public PaymentResponse initializePayment(PaymentRequest paymentRequest) { //Todo have to improve the accountancy

        User user = userRepo.findById(paymentRequest.user_id()).orElseThrow(() -> new ResourceNotFoundException("User is not registered"));
        Room room = roomRepo.findById(paymentRequest.room_id()).orElseThrow(() -> new ResourceNotFoundException("Room is not registered"));
        LocalDate checkIn = LocalDate.parse(paymentRequest.check_in(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate checkOut = LocalDate.parse(paymentRequest.check_out(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        BookingResponse bookingResponse = bookingService.startBooking(
                new BookingRequest(
                        paymentRequest.hotel_id(),
                        paymentRequest.room_id(),
                        paymentRequest.user_id(),
                        paymentRequest.total_guest(),
                        paymentRequest.check_in(),
                        paymentRequest.check_out()));

        if (!bookingResponse.isBookingPossible())
            return new PaymentResponse(null, PaymentStatus.FAILED, "room is not Available please select another room");

        List<Guest> savedGuests = new ArrayList<>();
        for (GuestRequest guestRequest : paymentRequest.guestDetails()) {
            Guest guest = new Guest();
            guest.setGender(Gender.valueOf(guestRequest.gender()));
            guest.setName(guestRequest.name());
            guest.setCreatedAt(LocalDateTime.now());
            guest.setUser(user);
            Guest savedGuest = guestService.addGuest(guest);
            savedGuests.add(savedGuest);
        }

        ThirdPartyPaymentResponse paymentResponse = ThirdPartyPayment(new ThirdPartyPaymentRequest(paymentRequest.user_id(), room.getBasePrice()));
        Payment payment = new Payment();
        payment.setPaymentStatus(paymentResponse.paymentStatus());
        payment.setPrice(room.getBasePrice());
        payment.setTransactionId(paymentResponse.transaction_id());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        Payment savedPayment = paymentRepo.save(payment);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setHotel(room.getHotel());
        booking.setBookingStatus(paymentResponse.paymentStatus() == PaymentStatus.SUCCESS ? BookingStatus.CONFIRMED : BookingStatus.CANCELLED);
        booking.setCreatedAt(LocalTime.now());
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setPayment(payment);
        Booking savedBooking = bookingService.saveBooking(booking);

        bookingGuestService.addRelationToBookingGuest(savedBooking, savedGuests);

        return new PaymentResponse(savedBooking.getId(), savedPayment.getPaymentStatus(), "Your room has Booked successfully");


    }


    public ThirdPartyPaymentResponse ThirdPartyPayment(ThirdPartyPaymentRequest thirdPartyPayementRequest) {
        return new ThirdPartyPaymentResponse("123456789", PaymentStatus.SUCCESS);
    }
}
