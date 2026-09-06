package com.gopi.airbnb.dto.response;

import com.gopi.airbnb.entitys.ContactInfo;

import java.util.List;

public record HotelGetResponse(Long id,
                               String city,
                               String name,
                               List<String> photos,
                               List<String> amenities,
                               Boolean active,
                               ContactInfo contactInfo) {
}