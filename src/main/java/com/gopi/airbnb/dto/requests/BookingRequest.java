package com.gopi.airbnb.dto.requests;

import com.gopi.airbnb.entitys.Guest;

import java.util.List;

public record BookingRequest(Long hotel_id,
                             Long room_id,
                             Long user_id,
                             Integer total_guest,
                             String check_in,
                             String check_out) {
}
