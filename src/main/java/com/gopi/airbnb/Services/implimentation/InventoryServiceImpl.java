package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.InventoryAddRequest;
import com.gopi.airbnb.dto.requests.InventoryUpdateRequest;
import com.gopi.airbnb.dto.response.InventoryAddResponse;
import com.gopi.airbnb.dto.response.InventoryFetchResponse;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.InventoryRepo;
import com.gopi.airbnb.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inventoryRepo;
    private final RoomRepo roomRepo;

    @Override
    public InventoryAddResponse addInventory(InventoryAddRequest request) {
        Room room = roomRepo.findById(request.roomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + request.roomId()));
        if(request.totalCount()<request.bookedCount())throw new InvalidDetailsException("You cannot reduce inventory totalCount below the current bookedCount");
        Inventory inventory = new Inventory();
        inventory.setDate(LocalDate.parse(request.date()));
        inventory.setBookedCount(request.bookedCount());
        inventory.setTotalCount(request.totalCount());
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setSurgeFactor(calculateSurgeFactor(request.totalCount(), request.bookedCount()));
        inventory.setClosed(!isBookingAvailable(request.totalCount(), request.bookedCount()));
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
        if(inventoryUpdateRequest.totalCount()<inventoryUpdateRequest.bookedCount())throw new InvalidDetailsException("You cannot reduce inventory totalCount below the current bookedCount");
        savedInventory.setDate(LocalDate.parse(inventoryUpdateRequest.date()));
        savedInventory.setBookedCount(inventoryUpdateRequest.bookedCount());
        savedInventory.setTotalCount(inventoryUpdateRequest.totalCount());
        savedInventory.setCreatedAt(savedInventory.getCreatedAt());
        savedInventory.setUpdatedAt(LocalDateTime.now());
        savedInventory.setSurgeFactor(calculateSurgeFactor(inventoryUpdateRequest.totalCount(), inventoryUpdateRequest.bookedCount()));
        savedInventory.setClosed(!isBookingAvailable(inventoryUpdateRequest.totalCount(), inventoryUpdateRequest.bookedCount()));
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
            if(inventoryUpdateRequest.totalCount()<savedInventory.getBookedCount())throw new InvalidDetailsException("You cannot reduce inventory totalCount below the current bookedCount");
            savedInventory.setTotalCount(inventoryUpdateRequest.totalCount());
        }
        if (inventoryUpdateRequest.bookedCount() != null) {
            savedInventory.setBookedCount(inventoryUpdateRequest.bookedCount());
        }
        savedInventory.setClosed(!isBookingAvailable(savedInventory.getTotalCount(),savedInventory.getBookedCount()));
        savedInventory.setSurgeFactor(calculateSurgeFactor(savedInventory.getTotalCount(),savedInventory.getBookedCount()));
        Inventory updatedInventory = inventoryRepo.save(savedInventory);
        return new InventoryAddResponse(updatedInventory.getId(), "Your Room Inventory  has updated successfully with inventory" + updatedInventory.getId());

    }

    @Override
    public void addInventoryByRoom(Integer bookingOpeningDaysCount, Room savedRoom) {

        for (int i = 0; i < bookingOpeningDaysCount; i++) {
            Inventory inventory = Inventory.builder()
                    .date(getDate(i))
                    .bookedCount(0)
                    .totalCount(savedRoom.getTotalCount())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .surgeFactor(calculateSurgeFactor(savedRoom.getTotalCount(), 0))
                    .closed(!isBookingAvailable(savedRoom.getTotalCount(), 0))
                    .room(savedRoom)
                    .build();
            Inventory savedInventory = inventoryRepo.save(inventory);
        }
    }



    @Override
    public void addLatestDateToRoomInventory(Room room, Integer bookingOpeningDaysCount) {
        List<Inventory> roomInventoryDataList = inventoryRepo.findByRoomIdAndDateGreaterThanEqualOrderByDateAsc(room.getId(), LocalDate.now());
        HashMap<LocalDate, Integer> datesDate = new HashMap<>();

        for (Inventory value : roomInventoryDataList) {
            datesDate.put(value.getDate(), 1);
        }
        for (int i = 0; i < bookingOpeningDaysCount; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            if (!datesDate.containsKey(date)) {
                Inventory inventory = Inventory.builder()
                        .date(date)
                        .bookedCount(0)
                        .totalCount(room.getTotalCount())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .surgeFactor(calculateSurgeFactor(room.getTotalCount(), 0))
                        .closed(!isBookingAvailable(room.getTotalCount(), 0))
                        .room(room)
                        .build();
                Inventory savedInventory = inventoryRepo.save(inventory);
                datesDate.put(date, 1);
            }
        }

    }

    @Override
    public void updateInventoryByRoom(Integer roomTotalCount,Long roomId) {
             List<Inventory> allInventoryFromToday=inventoryRepo.findByRoomIdAndDateGreaterThanEqualOrderByDateAsc(roomId,LocalDate.now());
             if(allInventoryFromToday.isEmpty())throw new ResourceNotFoundException("Inventory not found from the today date");
             for(Inventory inventory: allInventoryFromToday){
                  if(roomTotalCount>=inventory.getBookedCount()) {
                      inventory.setTotalCount(roomTotalCount);
                      inventory.setSurgeFactor(calculateSurgeFactor(roomTotalCount,inventory.getBookedCount()));
                      inventory.setClosed(!isBookingAvailable(roomTotalCount,inventory.getBookedCount()));
                      inventory.setUpdatedAt(LocalDateTime.now());
                  }
                  else{
                      throw new InvalidDetailsException("You cannot reduce inventory totalCount below the current bookedCount");
                  }


             }
             inventoryRepo.saveAll(allInventoryFromToday);
    }


    public LocalDate getDate(int distanceFromPresentDate) {
        return LocalDate.now().plusDays(distanceFromPresentDate);
    }

}
