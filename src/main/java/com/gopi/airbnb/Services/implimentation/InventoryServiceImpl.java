package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.requests.InventoryUpdateRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.dto.response.InventoryFetchResponse;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inventoryRepo;
    private final RoomService roomService;

    @Override
    public InventoryAddResponse addInventory(InventoryAddRequest request) {
        Room room = roomService.findByRoomId(request.roomId());
        Inventory inventory = new Inventory();
        inventory.setDate(LocalDate.parse(request.date()));
        inventory.setBookedCount(request.bookedCount());
        inventory.setTotalCount(request.totalCount());
        inventory.setCreatedAt(LocalDate.now());
        inventory.setUpdatedAt(LocalDate.now());
        inventory.setSurgeFactor(calculateSurgeFactor(request.totalCount(), request.bookedCount()));
        inventory.setClosed(isBookingAvailable(request.totalCount(), request.bookedCount()));
        inventory.setRoom(room);
        Inventory savedInventory = inventoryRepo.save(inventory);
        return new InventoryAddResponse(savedInventory.getId(), "Your Room Inventory  has added successfully with roomId" + request.roomId());
    }


    @Override
    public Double calculateSurgeFactor(Integer totalCount, Integer bookedCount) {
        if (totalCount == 0) return 1.0;

        Double occupancy = ((double) bookedCount / (double) totalCount);
        Double maxSurgeValue = 0.5;
        return 1.0 + (occupancy * maxSurgeValue);
    }

    @Override
    public Boolean isBookingAvailable(Integer totalCount, Integer bookedCount) {
        return bookedCount < totalCount;
    }

    @Override
    public List<InventoryFetchResponse> getInventoryByRoomId(Long roomId) {
        List<Inventory> inventoryList = inventoryRepo.findByRoomId(roomId);
        List<InventoryFetchResponse> inventoryResponseList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            InventoryFetchResponse inventoryFetchResponse = new InventoryFetchResponse(
                    inventory.getRoom().getId(),
                    inventory.getId(),
                    inventory.getDate(),
                    inventory.getBookedCount(),
                    inventory.getTotalCount());
            inventoryResponseList.add(inventoryFetchResponse);
        }
        return inventoryResponseList;

    }

    @Override
    public InventoryFetchResponse getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepo.findById(inventoryId).orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id" + inventoryId));
        return new InventoryFetchResponse(
                inventory.getRoom().getId(),
                inventory.getId(),
                inventory.getDate(),
                inventory.getBookedCount(),
                inventory.getTotalCount());
    }

    @Override
    public InventoryAddResponse deleteInventoryById(Long inventoryId) {
        inventoryRepo.deleteById(inventoryId);
        return new InventoryAddResponse(inventoryId, "Inventory has deleted successfully with id " + inventoryId);
    }

    @Override
    public InventoryAddResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {

        Inventory savedInventory = inventoryRepo.findById(inventoryUpdateRequest.inventory_id()).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found with id" + inventoryUpdateRequest.inventory_id())
        );

        savedInventory.setDate(LocalDate.parse(inventoryUpdateRequest.date()));
        savedInventory.setBookedCount(inventoryUpdateRequest.bookedCount());
        savedInventory.setTotalCount(inventoryUpdateRequest.totalCount());
        savedInventory.setCreatedAt(savedInventory.getCreatedAt());
        savedInventory.setUpdatedAt(LocalDate.now());
        savedInventory.setSurgeFactor(calculateSurgeFactor(inventoryUpdateRequest.totalCount(), inventoryUpdateRequest.bookedCount()));
        savedInventory.setClosed(isBookingAvailable(inventoryUpdateRequest.totalCount(), inventoryUpdateRequest.bookedCount()));
        savedInventory.setRoom(savedInventory.getRoom());
        Inventory updatedInventory = inventoryRepo.save(savedInventory);
        return new InventoryAddResponse(updatedInventory.getId(), "Your Room Inventory  has updated successfully with inventory" + updatedInventory.getId());
    }

    @Override
    public InventoryAddResponse updateInventoryField(InventoryUpdateRequest inventoryUpdateRequest) {
        Inventory savedInventory = inventoryRepo.findById(inventoryUpdateRequest.inventory_id()).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found with id" + inventoryUpdateRequest.inventory_id()));
        if (inventoryUpdateRequest.date() != null) {
            savedInventory.setDate(LocalDate.parse(inventoryUpdateRequest.date()));
        }
        if (inventoryUpdateRequest.totalCount() != null) {
            savedInventory.setTotalCount(inventoryUpdateRequest.totalCount());
        }
        if (inventoryUpdateRequest.bookedCount() != null) {
            savedInventory.setBookedCount(inventoryUpdateRequest.bookedCount());
        }

        savedInventory.setSurgeFactor(calculateSurgeFactor(savedInventory.getTotalCount(), savedInventory.getBookedCount()));
        Inventory updatedInventory = inventoryRepo.save(savedInventory);
        return new InventoryAddResponse(updatedInventory.getId(), "Your Room Inventory  has updated successfully with inventory" + updatedInventory.getId());

    }





}
