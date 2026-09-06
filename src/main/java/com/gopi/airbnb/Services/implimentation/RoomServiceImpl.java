package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.Services.InventoryService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import com.gopi.airbnb.dto.response.RoomFetchResponse;
import com.gopi.airbnb.entitys.Hotel;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepo roomRepo;
    private final HotelService hotelService;


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
}
