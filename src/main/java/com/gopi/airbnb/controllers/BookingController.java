package com.gopi.airbnb.controllers;

import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/bookings")
public class BookingController {
     private final BookingService bookingService;



    @PostMapping("/startBooking")
    ResponseEntity<BookingResponse>startBooking(@RequestBody BookingRequest bookingRequest){

        BookingResponse bookingResponse= bookingService.startBooking(bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

}
