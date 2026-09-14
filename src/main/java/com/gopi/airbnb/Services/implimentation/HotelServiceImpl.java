package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.ContactInfoService;
import com.gopi.airbnb.Services.HotelService;
import com.gopi.airbnb.Services.RoomService;
import com.gopi.airbnb.dto.requests.HotelCreationRequest;
import com.gopi.airbnb.dto.requests.HotelSearchRequest;
import com.gopi.airbnb.dto.requests.HotelUpdateRequest;
import com.gopi.airbnb.dto.response.HotelCreationResponse;
import com.gopi.airbnb.dto.response.HotelGetResponse;
import com.gopi.airbnb.entitys.ContactInfo;
import com.gopi.airbnb.entitys.Hotel;
import com.gopi.airbnb.entitys.Inventory;
import com.gopi.airbnb.entitys.Room;
import com.gopi.airbnb.exceptions.ResourceAlreadyExistsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.HotelRepo;
import com.gopi.airbnb.repository.InventoryRepo;
import com.gopi.airbnb.repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final ContactInfoService contactInfoService;
    private final HotelRepo hotelRepo;
    private final RoomRepo roomRepo;
    private final InventoryRepo inventoryRepo;


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
        hotel.setName(request.name().toLowerCase());
        hotel.setCity(request.city().toLowerCase());
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

    @Override
    public HotelGetResponse getHotelById(Long hotelId) {
        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        return new HotelGetResponse(
                hotelId,
                hotel.getCity(),
                hotel.getName(),
                hotel.getPhotos(),
                hotel.getAmenities(),
                hotel.getActive(),
                hotel.getContact_info());
    }

    @Override
    public HotelCreationResponse updateHotel(HotelUpdateRequest hotelUpdateRequest) {
        Hotel hotelSaved = hotelRepo.findById(hotelUpdateRequest.hotel_id())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelUpdateRequest.hotel_id()));
        ContactInfo hotelcontactInfo = contactInfoService.updateContactInfo(hotelSaved.getContact_info().getId(), hotelUpdateRequest.contactInfo());

        Hotel hotel = new Hotel();
        hotel.setId(hotelSaved.getId());
        hotel.setName(hotelUpdateRequest.name().toLowerCase());
        hotel.setCity(hotelUpdateRequest.city().toLowerCase());
        hotel.setAmenities(hotelUpdateRequest.amenities());
        hotel.setPhotos(hotelUpdateRequest.photos());
        hotel.setActive(true);
        hotel.setCreatedAt(hotelSaved.getCreatedAt());
        hotel.setUpdatedAt(LocalDateTime.now());
        hotel.setContact_info(hotelcontactInfo);

        Hotel updatedHotel = hotelRepo.save(hotel);

        return new HotelCreationResponse(updatedHotel.getId(), "Hotel is Updated successfully");

    }

    @Override
    public HotelCreationResponse updateHotelField(HotelUpdateRequest hotelUpdateRequest) {
        Hotel hotelSaved = hotelRepo.findById(hotelUpdateRequest.hotel_id())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelUpdateRequest.hotel_id()));

        if (hotelUpdateRequest.contactInfo() != null) {
            ContactInfo hotelcontactInfo = contactInfoService.updateContactInfoField(hotelSaved.getContact_info().getId(), hotelUpdateRequest.contactInfo());
            hotelSaved.setContact_info(hotelcontactInfo);
        }
        if (hotelUpdateRequest.city() != null) {
            hotelSaved.setCity(hotelUpdateRequest.city());
        }
        if (hotelUpdateRequest.name() != null) {
            hotelSaved.setName(hotelUpdateRequest.name());
        }

        if (hotelUpdateRequest.amenities() != null) {
            hotelSaved.setAmenities(hotelUpdateRequest.amenities());
        }

        if (hotelUpdateRequest.photos() != null) {
            hotelSaved.setPhotos(hotelUpdateRequest.photos());
        }
        hotelSaved.setUpdatedAt(LocalDateTime.now());

        Hotel hotelUpdated = hotelRepo.save(hotelSaved);
        return new HotelCreationResponse(hotelUpdated.getId(), "Hotel Details Updated Successfully");

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HotelCreationResponse deleteHotel(Long hotelId) {
        //  roomService.deleteByHotelId(hotelId);
        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel is Not registered"));
        contactInfoService.deleteById(hotel.getContact_info().getId());
        hotelRepo.deleteById(hotelId);
        return new HotelCreationResponse(hotelId, "Hotel is Deleted Successfully");
    }

    @Override
    public List<HotelGetResponse> hotelSearch(HotelSearchRequest hotelSearchRequest) {
        System.out.println("entered hear");
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkIn= LocalDate.parse(hotelSearchRequest.checkIn(),formatter);

        LocalDate checkOut= LocalDate.parse(hotelSearchRequest.checkOut(),formatter);
        Integer guestCount= hotelSearchRequest.guestCount();
        List<Hotel> hotelInCityList = hotelRepo.findByCity(hotelSearchRequest.city());
        List<HotelGetResponse> hotelGetResponseList = new ArrayList<>();
        for (Hotel hotel : hotelInCityList) {
            HotelGetResponse hotelGetResponse = new HotelGetResponse(hotel.getId(), hotel.getCity(), hotel.getName(), hotel.getPhotos(), hotel.getAmenities(), hotel.getActive(), hotel.getContact_info());
            List<Room> roomList= roomRepo.findByHotelId(hotelGetResponse.id());
            for(Room room:roomList){
                List<Inventory>inventoryList= inventoryRepo.findByRoomIdAndDateBetween(room.getId(),checkIn,checkOut);
                boolean isEmpty=true;
                for(Inventory inventory: inventoryList){
                    System.out.println(inventory.getDate());
                    if(inventory.getTotalCount()-inventory.getBookedCount()<guestCount){
                        isEmpty=false;
                    }
                }
                if(isEmpty && !inventoryList.isEmpty()){
                    hotelGetResponseList.add(hotelGetResponse);
                }
            }


        }
        return hotelGetResponseList;
    }
}
