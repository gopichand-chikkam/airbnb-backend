package com.gopi.airbnb.controllers;


import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/hotel")
public class HotelController {
      private final HotelService hotelService;

    @PostMapping("/addHotel")
    ResponseEntity<HotelCreationResponse>addHotel(@Valid @RequestBody HotelCreationRequest hotelCreationRequest){

        HotelCreationResponse  hotelCreationResponse=  hotelService.addHotel(hotelCreationRequest);
        return ResponseEntity.ok(hotelCreationResponse);
    }



}
