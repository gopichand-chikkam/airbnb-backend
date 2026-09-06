package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Double calculateSurgeFactor(Integer totalCount, Integer bookedCount) {
        if (totalCount == 0) return 1.0;

        Double occupancy = (double) (bookedCount /totalCount );
        Double maxSurgeValue = 0.5;
        return 1.0 + (occupancy * maxSurgeValue);
    }

    public Boolean isBookingAvailable(Integer totalCount, Integer bookedCount) {
        return bookedCount < totalCount;
    }
}
