package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.requests.RoomUpdateRequest;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import com.gopi.airbnb.dto.response.RoomFetchResponse;
import com.gopi.airbnb.entitys.Hotel;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final HotelService hotelService;
    private final InventoryService inventoryService;


    @Override
    public RoomAddResponse addRoom(RoomAddRequest request) {
        Hotel hotel = hotelService.findByHotelId(request.hotel_id());
        Room room = new Room();
        room.setType(request.type());
        room.setHotel(hotel);
        room.setAmenities(request.amenities());
        room.setBasePrice(request.basePrice());
        room.setUpdatedAt(LocalDateTime.now());
        room.setCreatedAt(LocalDateTime.now());
        room.setCapacity(request.capacity());
        room.setTotalCount(request.totalCount());
        room.setPhotos(request.photos());
        Room savedRoom = roomRepo.save(room);

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

    @Override
    public RoomAddResponse updateRoom(RoomUpdateRequest roomUpdateRequest) {
        Room savedRoom = roomRepo.findById(roomUpdateRequest.room_id()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id" + roomUpdateRequest.room_id()));

        savedRoom.setType(roomUpdateRequest.type());
        savedRoom.setHotel(savedRoom.getHotel());
        savedRoom.setAmenities(roomUpdateRequest.amenities());
        savedRoom.setBasePrice(roomUpdateRequest.basePrice());
        savedRoom.setUpdatedAt(LocalDateTime.now());
        savedRoom.setCreatedAt(savedRoom.getCreatedAt());
        savedRoom.setCapacity(roomUpdateRequest.capacity());
        savedRoom.setTotalCount(roomUpdateRequest.totalCount());
        savedRoom.setPhotos(roomUpdateRequest.photos());
        Room updatecRoom = roomRepo.save(savedRoom);
        return new RoomAddResponse(savedRoom.getId(), "Room is successfully added " + updatecRoom.getId());
    }

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
        }

        if (roomUpdateRequest.type() != null) {
            savedRoom.setType(roomUpdateRequest.type());
        }
        Room updatecRoom = roomRepo.save(savedRoom);
        return new RoomAddResponse(savedRoom.getId(), "Room is successfully updated " + updatecRoom.getId());
    }
}
