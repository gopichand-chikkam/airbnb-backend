package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.entitys.Hotel;

import java.util.Optional;

public interface HotelService {
    HotelCreationResponse addHotel(HotelCreationRequest hotelCreationRequest);


    Hotel findByHotelId(Long hotelId);
}
