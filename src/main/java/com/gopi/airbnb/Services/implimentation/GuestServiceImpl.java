package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.GuestService;
import com.gopi.airbnb.entitys.Guest;
import com.gopi.airbnb.repository.GuestRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
       private final GuestRepo guestRepo;

    @Override
    public Guest addGuest(Guest guest) {

            return guestRepo.save(guest);

    }
}
