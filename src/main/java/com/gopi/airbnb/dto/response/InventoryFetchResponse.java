package com.gopi.airbnb.dto.response;

import java.time.LocalDate;

public record InventoryFetchResponse(Long room_id,
                                     Long inventory_id,
                                     LocalDate date,
                                     Integer bookedCount,
                                     Integer totalCount) {
}
