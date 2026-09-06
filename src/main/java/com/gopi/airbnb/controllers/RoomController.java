package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
