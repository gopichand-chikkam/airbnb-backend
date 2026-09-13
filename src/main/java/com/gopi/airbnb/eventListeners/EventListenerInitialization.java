package com.gopi.airbnb.eventListeners;

import com.gopi.airbnb.Services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class EventListenerInitialization {
    private final RoomService roomService;

    @EventListener(ApplicationReadyEvent.class)
    public void updateLatestDateOfRoomInventoryEvent(){
       roomService.updateLatestDateOfRoomInventory();
    }
    
}
