package com.gopi.airbnb.eventListeners;

import com.gopi.airbnb.Services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventListenerInitialization {
    private final RoomService roomService;

    @EventListener(ApplicationReadyEvent.class)
    public void updateLatestDateOfRoomInventoryEvent(){
        System.out.println("event listner started");
       roomService.updateLatestDateOfRoomInventory();
    }
    
}
