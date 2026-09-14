package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.requests.HotelSearchRequest;
import com.gopi.airbnb.dto.requests.HotelUpdateRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.dto.response.HotelGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/hotel")
public class HotelController {
    private final HotelService hotelService;

    @PostMapping("/addHotel")
    ResponseEntity<HotelCreationResponse> addHotel(@Valid @RequestBody HotelCreationRequest hotelCreationRequest) {

        HotelCreationResponse hotelCreationResponse = hotelService.addHotel(hotelCreationRequest);
        return ResponseEntity.ok(hotelCreationResponse);
    }


    @GetMapping("/getHotel/{hotel_id}")
    ResponseEntity<HotelGetResponse> getHotel(@PathVariable Long hotel_id) {
        HotelGetResponse hotelGetResponse = hotelService.getHotelById(hotel_id);

        return ResponseEntity.ok(hotelGetResponse);
    }

    @PutMapping("/updateHotel")
    ResponseEntity<HotelCreationResponse> updateHotel(@Valid @RequestBody HotelUpdateRequest hotelUpdateRequest) {

        HotelCreationResponse hotelCreationResponse = hotelService.updateHotel(hotelUpdateRequest);
        return ResponseEntity.ok(hotelCreationResponse);
    }

    @PatchMapping("/updateHotelField")
    ResponseEntity<HotelCreationResponse> updateHotelField(@RequestBody HotelUpdateRequest hotelUpdateRequest) {
        HotelCreationResponse hotelCreationResponse = hotelService.updateHotelField(hotelUpdateRequest);
        return ResponseEntity.ok(hotelCreationResponse);
    }

    @DeleteMapping("/deleteHotel/{hotel_id}")
    ResponseEntity<HotelCreationResponse>delete(@PathVariable Long hotel_id){
         HotelCreationResponse hotelCreationResponse= hotelService.deleteHotel(hotel_id);
         return ResponseEntity.ok(hotelCreationResponse);
    }

    @GetMapping("/hotelSearch")
    ResponseEntity<List<HotelGetResponse>>getHotelSearch(@RequestBody HotelSearchRequest hotelSearchRequest){
        List<HotelGetResponse> hotelList= hotelService.hotelSearch(hotelSearchRequest);
        return ResponseEntity.ok(hotelList);
    }


}
