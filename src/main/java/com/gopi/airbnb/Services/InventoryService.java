package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.requests.InventoryUpdateRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.dto.response.InventoryFetchResponse;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;

import java.util.List;

public interface InventoryService {

    InventoryAddResponse addInventory(InventoryAddRequest request);
     Double calculateSurgeFactor(Integer totalCount, Integer bookedCount);
    Boolean isBookingAvailable(Integer totalCount, Integer bookedCount);

    List<InventoryFetchResponse> getInventoryByRoomId(Long roomId);

    InventoryFetchResponse getInventoryById(Long inventoryId);

    InventoryAddResponse deleteInventoryById(Long inventoryId);

    InventoryAddResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest);

    InventoryAddResponse updateInventoryField(InventoryUpdateRequest inventoryUpdateRequest);

    void addInventoryByRoom(Integer bookingOpeningDaysCount, Room savedRoom);

    void addLatestDateToRoomInventory(Room room,Integer bookingOpeningDaysCount);
}
