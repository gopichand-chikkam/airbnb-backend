package com.gopi.airbnb.dto.requests;

import java.util.List;

public record PaymentRequest( Long hotel_id,
                             Long room_id,
                             Long user_id,
                             Integer total_guest,
                             List<GuestRequest> guestDetails,
                             String check_in,
                             String check_out){}