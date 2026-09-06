package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.dto.response.InventoryFetchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/getInventoryByRommId/{room_id}")
    ResponseEntity<List<InventoryFetchResponse>>getInventoryByRoomId(@PathVariable Long room_id){
         List<InventoryFetchResponse> response= inventoryService.getInventoryByRoomId(room_id);
         return ResponseEntity.ok(response);
    }

    @GetMapping("/getInventory/{inventory_id}")
    ResponseEntity<InventoryFetchResponse>getInventory(@PathVariable Long inventory_id){
        InventoryFetchResponse response= inventoryService.getInventoryById(inventory_id);
        return ResponseEntity.ok(response);
    }



}
