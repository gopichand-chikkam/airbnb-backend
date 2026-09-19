package com.gopi.airbnb.Services;


import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.entitys.Booking;

public interface BookingService {
    BookingResponse startBooking(BookingRequest bookingRequest);


    Booking saveBooking(Booking booking);
}
