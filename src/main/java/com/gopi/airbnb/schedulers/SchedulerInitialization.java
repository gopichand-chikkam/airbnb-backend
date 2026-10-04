package com.gopi.airbnb.schedulers;

import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.Services.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerInitialization {
    private final RoomService roomService;
    private  final BookingService bookingService;

    @Scheduled(cron = "0 1 0 * * ?")
    public void updateLatestDateOfRoomInventoryScheduler(){
        roomService.updateLatestDateOfRoomInventory();
    }

    @Scheduled(fixedRate = 5000)
    public void updateReservationAndInventoryScheduler(){
        bookingService.updateReservationAndInventory();
    }
}
