package com.gopi.airbnb.dto.requests;

import java.math.BigDecimal;
import java.util.List;

public record RoomAddRequest(
        Long hotel_id,
        String type,
        BigDecimal basePrice,
        List<String> photos,
        List<String>amenities,
        Integer totalCount,
        Integer capacity
) {
}
