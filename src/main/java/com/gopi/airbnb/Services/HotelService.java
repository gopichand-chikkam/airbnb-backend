package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.requests.HotelSearchRequest;
import com.gopi.airbnb.dto.requests.HotelUpdateRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.dto.response.HotelGetResponse;
import com.gopi.airbnb.entitys.Hotel;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    HotelCreationResponse addHotel(HotelCreationRequest hotelCreationRequest);


    Hotel findByHotelId(Long hotelId);

    HotelGetResponse getHotelById(Long hotelId);

    HotelCreationResponse updateHotel(@Valid HotelUpdateRequest hotelUpdateRequest);

    HotelCreationResponse updateHotelField(HotelUpdateRequest hotelUpdateRequest);

    HotelCreationResponse deleteHotel(Long hotelId);

    List<HotelGetResponse> hotelSearch(HotelSearchRequest hotelSearchRequest);
}
