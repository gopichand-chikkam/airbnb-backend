package com.gopi.airbnb.dto.requests;

import java.util.List;

public record RoomUpdateRequest(Long room_id,
                                String type,
                                Double basePrice,
                                List<String> photos,
                                List<String>amenities,
                                Integer totalCount,
                                Integer capacity) {
}
