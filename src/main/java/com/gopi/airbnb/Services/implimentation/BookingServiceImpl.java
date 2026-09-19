package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.dto.requests.BookingRequest;
import com.gopi.airbnb.dto.requests.GuestRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.entitys.*;
import com.gopi.airbnb.enums.Gender;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepo bookingRepo;
    private final InventoryRepo inventoryRepo;




    @Override
    public BookingResponse startBooking(BookingRequest bookingRequest) {
        LocalDate checkIn = LocalDate.parse(bookingRequest.check_in(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate checkOut = LocalDate.parse(bookingRequest.check_out(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Inventory> inventoryList = inventoryRepo.findByRoomIdAndDateBetween(bookingRequest.room_id(), checkIn, checkOut);
        for (Inventory inventory : inventoryList) {
            if (inventory.getTotalCount() - inventory.getBookedCount() < 1) return new BookingResponse(false);
        }

        return new BookingResponse(true);
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepo.save(booking);
    }


}


