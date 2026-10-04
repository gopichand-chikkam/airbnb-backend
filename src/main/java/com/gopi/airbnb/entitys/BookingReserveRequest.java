package com.gopi.airbnb.entitys;

import com.gopi.airbnb.dto.requests.GuestRequest;

import java.util.List;

public record BookingReserveRequest(Long hotel_id,
                                    Long room_id,
                                    Long user_id,
                                    Integer total_guest,
                                    List<GuestRequest> guestDetails,
                                    String check_in,
                                    String check_out) {
}
