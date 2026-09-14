package com.gopi.airbnb.dto.requests;

import java.time.LocalDate;

public record HotelSearchRequest(String city,
                                 String checkIn,
                                 String checkOut,
                                 Integer guestCount) {
}
