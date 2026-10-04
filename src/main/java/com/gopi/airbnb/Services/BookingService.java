package com.gopi.airbnb.Services;


import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.entitys.Booking;
import com.gopi.airbnb.entitys.BookingReserveRequest;

public interface BookingService {
    BookingResponse startBooking(BookingRequest bookingRequest);


    Booking saveBooking(Booking booking);
    boolean checkAvailabilityAndCreateReservation(BookingRequest bookingRequest);

    BookingResponse reserveBooking(BookingReserveRequest bookingReserveRequest);
     void updateReservationAndInventory();
}
