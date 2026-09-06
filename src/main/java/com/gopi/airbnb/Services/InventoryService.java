package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.entitys.Inventory;

public interface InventoryService {

    InventoryAddResponse addInventory(InventoryAddRequest request);
}
