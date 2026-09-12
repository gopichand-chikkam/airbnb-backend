package com.gopi.airbnb.dto.requests;

public record InventoryUpdateRequest(
        Long inventory_id,
        String date,
        Integer bookedCount,
        Integer totalCount) {
}
