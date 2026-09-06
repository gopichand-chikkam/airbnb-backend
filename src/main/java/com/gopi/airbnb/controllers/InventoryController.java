package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin/hotel/room/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @PostMapping ("/addInventory")
    ResponseEntity<InventoryAddResponse>addInventory(@RequestBody InventoryAddRequest request){
           InventoryAddResponse inventoryAddResponse= inventoryService.addInventory(request);
        return ResponseEntity.ok(inventoryAddResponse);
    }

}
