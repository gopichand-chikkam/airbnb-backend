package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.HotelUpdateRequest;
import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.requests.RoomUpdateRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import com.gopi.airbnb.dto.response.RoomFetchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/hotel/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

        @PostMapping("/addRoom")
        ResponseEntity<RoomAddResponse> addRoom(@RequestBody RoomAddRequest request){
             RoomAddResponse roomAddResponse= roomService.addRoom(request);
             return ResponseEntity.ok(roomAddResponse);
        }

        @GetMapping("/getRoomsByHotel/{hotel_id}")
       ResponseEntity<List<RoomFetchResponse>> fetchRoomList(@PathVariable Long hotel_id){
            List<RoomFetchResponse> roomFetchResponse= roomService.findByHotelId(hotel_id);
            return ResponseEntity.ok(roomFetchResponse);
        }

        @GetMapping("/getRooms/{room_id}")
        ResponseEntity<RoomFetchResponse> fetchRoom(@PathVariable Long room_id){
            RoomFetchResponse roomFetchResponse = roomService.getByRoomId(room_id);
            return ResponseEntity.ok(roomFetchResponse);
        }

    @PutMapping("/updateRoom")
    ResponseEntity<RoomAddResponse> updateRoom( @RequestBody RoomUpdateRequest roomUpdateRequest) {

        RoomAddResponse roomAddResponse = roomService.updateRoom(roomUpdateRequest);
        return ResponseEntity.ok(roomAddResponse);
    }

    @PatchMapping("/updateRoomFeild")
    ResponseEntity<RoomAddResponse> updateRoomField(@RequestBody RoomUpdateRequest roomUpdateRequest) {
        RoomAddResponse roomAddResponse = roomService.updateRoomField(roomUpdateRequest);
        return ResponseEntity.ok(roomAddResponse);
    }
        @DeleteMapping("/delRoom/{room_id}")
       ResponseEntity<RoomAddResponse>delRoom(@PathVariable Long room_id){
            RoomAddResponse roomAddResponse = roomService.deleteRoomById(room_id);
            return ResponseEntity.ok(roomAddResponse);
        }


}
