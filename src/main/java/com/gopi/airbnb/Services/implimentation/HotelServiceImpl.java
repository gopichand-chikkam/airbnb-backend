package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.ContactInfoService;
import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.entitys.ContactInfo;
import com.gopi.airbnb.entitys.Hotel;
import com.gopi.airbnb.exceptions.ResourceAlreadyExistsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.HotelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final ContactInfoService contactInfoService;
    private final HotelRepo hotelRepo;

    @Override
    public HotelCreationResponse addHotel(HotelCreationRequest request) {

        if (contactInfoService.checkPhoneNumberExists(request.contactInfo().getPhoneNumber())) {
            throw new ResourceAlreadyExistsException("Phone number already registered with another hotel");
        }
        if (contactInfoService.checkEmailExists(request.contactInfo().getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered with another hotel");
        }
        ContactInfo hotelcontactInfo = contactInfoService.addContactDetails(request.contactInfo());

        Hotel hotel = new Hotel();
        hotel.setName(request.name());
        hotel.setCity(request.city());
        hotel.setAmenities(request.amenities());
        hotel.setPhotos(request.photos());
        hotel.setActive(true);
        hotel.setCreatedAt(LocalDateTime.now());
        hotel.setUpdatedAt(LocalDateTime.now());
        hotel.setContact_info(hotelcontactInfo);

        Hotel savedHotel = hotelRepo.save(hotel);
        return new HotelCreationResponse(savedHotel.getId(), "Hotel has Added Successfully");
    }

    @Override
    public Hotel findByHotelId(Long hotelId) {
        return hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
    }


}
