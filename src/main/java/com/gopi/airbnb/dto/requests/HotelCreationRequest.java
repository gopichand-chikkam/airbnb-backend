package com.gopi.airbnb.dto.requests;

import com.gopi.airbnb.entitys.ContactInfo;
import com.gopi.airbnb.entitys.Room;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record HotelCreationRequest(
        @NotBlank(message = "city is Required")
        String city,
        @NotBlank(message = "Name is Required")
        String name,
        ContactInfo contactInfo,
        List<String> photos,
        List<String>amenities,
        List<Room>rooms) {
}
