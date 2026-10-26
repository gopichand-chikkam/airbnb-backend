package com.gopi.airbnb.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RoomFetchResponse(Long hotel_id, Long id, String type, BigDecimal basePrice, List<String> photos,
                                List<String> amenities, Integer totalCount, Integer capacity) {
}
