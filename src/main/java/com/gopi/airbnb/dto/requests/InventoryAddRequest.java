package com.gopi.airbnb.dto.requests;

public record InventoryAddRequest(
        Long roomId,
        String date,
        Integer bookedCount,
        Integer totalCount) {
}
