package com.gopi.airbnb.dto.functionalDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PricingDetails(BigDecimal basePrice, LocalDate checkIn,
                             LocalDate checkOut,
                             Integer totalGuests,
                             Integer totalRooms,
                              Integer bookedRooms,
                             Boolean holiday,
                             Boolean discountActive) {
}
