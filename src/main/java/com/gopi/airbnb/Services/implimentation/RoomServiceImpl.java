package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.requests.RoomUpdateRequest;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import com.gopi.airbnb.dto.response.RoomFetchResponse;
import com.gopi.airbnb.entitys.Hotel;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.ResourceAlreadyExistsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final HotelService hotelService;
    private final InventoryService inventoryService;
    private final Integer bookingOpeningDaysCount = 30;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomAddResponse addRoom(RoomAddRequest request) {
        Hotel hotel = hotelService.findByHotelId(request.hotel_id());
        boolean alreadySameTypeRoom = roomRepo.findByHotelIdAndType(request.hotel_id(), request.type().toLowerCase()).isPresent();
        if (alreadySameTypeRoom) throw new ResourceAlreadyExistsException("Already this room type is present");
        Room room = Room.builder()
                .type(request.type().toLowerCase())
                .hotel(hotel)
                .amenities(request.amenities())
                .basePrice(request.basePrice())
                .capacity(request.capacity())
                .totalCount(request.totalCount())
                .photos(request.photos())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        Room savedRoom = roomRepo.save(room);
        inventoryService.addInventoryByRoom(bookingOpeningDaysCount, savedRoom);
        return new RoomAddResponse(savedRoom.getId(), "Room is successfully added in Hotel" + hotel.getName());
    }

    @Override
    public Room findByRoomId(Long roomId) {
        return roomRepo.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + roomId));
    }

    @Override
    public List<RoomFetchResponse> findByHotelId(Long hotelId) {
        List<Room> roomsData = roomRepo.findByHotelId(hotelId);
        List<RoomFetchResponse> roomFetchResponseList = new ArrayList<>();
        for (Room room : roomsData) {
            RoomFetchResponse roomFetchResponse = new RoomFetchResponse(hotelId,
                    room.getId(),
                    room.getType(),
                    room.getBasePrice(),
                    room.getPhotos(),
                    room.getAmenities(),
                    room.getTotalCount(),
                    room.getCapacity());
            roomFetchResponseList.add(roomFetchResponse);
        }
        return roomFetchResponseList;
    }


    @Override
    public RoomFetchResponse getByRoomId(Long roomId) {
        Room room = roomRepo.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + roomId));

        return new RoomFetchResponse(room.getHotel().getId(),
                room.getId(),
                room.getType(),
                room.getBasePrice(),
                room.getPhotos(),
                room.getAmenities(),
                room.getTotalCount(),
                room.getCapacity());
    }

    @Override
    public RoomAddResponse deleteRoomById(Long roomId) {
        roomRepo.deleteById(roomId);
        return new RoomAddResponse(roomId, "Room has deleted successfully with id " + roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomAddResponse updateRoom(RoomUpdateRequest roomUpdateRequest) {
        Room savedRoom = roomRepo.findById(roomUpdateRequest.room_id()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + roomUpdateRequest.room_id()));
        boolean isToUpdateInventory = !Objects.equals(savedRoom.getTotalCount(), roomUpdateRequest.totalCount());
        savedRoom.setType(roomUpdateRequest.type());
        savedRoom.setHotel(savedRoom.getHotel());
        savedRoom.setAmenities(roomUpdateRequest.amenities());
        savedRoom.setBasePrice(roomUpdateRequest.basePrice());
        savedRoom.setUpdatedAt(LocalDateTime.now());
        savedRoom.setCreatedAt(savedRoom.getCreatedAt());
        savedRoom.setCapacity(roomUpdateRequest.capacity());
        savedRoom.setPhotos(roomUpdateRequest.photos());
        savedRoom.setTotalCount(roomUpdateRequest.totalCount());
        Room updatecRoom = roomRepo.save(savedRoom);
        if (isToUpdateInventory)
            inventoryService.updateInventoryByRoom(roomUpdateRequest.totalCount(), roomUpdateRequest.room_id());
        return new RoomAddResponse(savedRoom.getId(), "Room is successfully added " + updatecRoom.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomAddResponse updateRoomField(RoomUpdateRequest roomUpdateRequest) {
        Room savedRoom = roomRepo.findById(roomUpdateRequest.room_id()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + roomUpdateRequest.room_id()));
        if (roomUpdateRequest.capacity() != null) {
            savedRoom.setCapacity(roomUpdateRequest.capacity());
        }

        if (roomUpdateRequest.amenities() != null) {
            savedRoom.setAmenities(roomUpdateRequest.amenities());
        }

        if (roomUpdateRequest.photos() != null) {
            savedRoom.setPhotos(roomUpdateRequest.photos());
        }

        if (roomUpdateRequest.basePrice() != null) {
            savedRoom.setBasePrice(roomUpdateRequest.basePrice());
        }

        if (roomUpdateRequest.totalCount() != null) {
            savedRoom.setTotalCount(roomUpdateRequest.totalCount());
            inventoryService.updateInventoryByRoom(roomUpdateRequest.totalCount(), roomUpdateRequest.room_id());
        }

        if (roomUpdateRequest.type() != null) {
            savedRoom.setType(roomUpdateRequest.type());
        }
        Room updatecRoom = roomRepo.save(savedRoom);
        return new RoomAddResponse(savedRoom.getId(), "Room is successfully updated " + updatecRoom.getId());
    }

    @Override
    public void updateLatestDateOfRoomInventory() {
        List<Room> allRooms = roomRepo.findAll();
        for (Room room : allRooms) {
            inventoryService.addLatestDateToRoomInventory(room, bookingOpeningDaysCount);
        }

    }
}
