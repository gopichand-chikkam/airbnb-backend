package com.gopi.airbnb.Services;


import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.dto.response.ReserveBookingResponse;
import com.gopi.airbnb.entitys.Booking;
import com.gopi.airbnb.entitys.BookingReserveRequest;
import com.gopi.airbnb.entitys.Reservation;

public interface BookingService {
    BookingResponse startBooking(BookingRequest bookingRequest);


    Booking saveBooking(Booking booking);
    boolean checkAvailabilityAndCreateReservation(BookingRequest bookingRequest);

    ReserveBookingResponse reserveBooking(BookingReserveRequest bookingReserveRequest);
     void updateReservationAndInventory();
     void updateReservationAndInventoryByReservationStatus(Reservation reservation, boolean isReservationExpired);
}
