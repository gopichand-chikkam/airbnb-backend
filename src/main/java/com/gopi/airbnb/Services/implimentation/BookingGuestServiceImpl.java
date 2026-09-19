package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.BookingGuestService;
import com.gopi.airbnb.entitys.Booking;
import com.gopi.airbnb.entitys.BookingGuest;
import com.gopi.airbnb.entitys.Guest;
import com.gopi.airbnb.repository.BookingGuestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingGuestServiceImpl implements BookingGuestService {
    private final BookingGuestRepo bookingGuestRepo;

    @Override
    public void addRelationToBookingGuest(Booking savedBooking, List<Guest> savedGuests) {
        for (Guest guest : savedGuests) {
            BookingGuest bookingGuest = new BookingGuest();
            bookingGuest.setGuest(guest);
            bookingGuest.setBooking(savedBooking);
            BookingGuest savedBookingGuest = bookingGuestRepo.save(bookingGuest);

        }
    }
}
