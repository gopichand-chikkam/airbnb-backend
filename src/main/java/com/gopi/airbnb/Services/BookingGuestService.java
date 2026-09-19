package com.gopi.airbnb.Services;

import com.gopi.airbnb.entitys.Booking;
import com.gopi.airbnb.entitys.Guest;

import java.util.List;

public interface BookingGuestService {
    void addRelationToBookingGuest(Booking savedBooking, List<Guest> savedGuests);
}
