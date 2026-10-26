package com.gopi.airbnb.Services.implimentation;

import com.gopi.airbnb.Services.BookingService;
import com.gopi.airbnb.Services.GuestService;
import com.gopi.airbnb.dto.requests.BookingRequest;

import com.gopi.airbnb.dto.requests.GuestRequest;
import com.gopi.airbnb.dto.response.BookingResponse;
import com.gopi.airbnb.dto.response.ReserveBookingResponse;
import com.gopi.airbnb.entitys.*;
import com.gopi.airbnb.enums.Gender;
import com.gopi.airbnb.enums.ReservationStatus;
import com.gopi.airbnb.exceptions.InvalidDetailsException;
import com.gopi.airbnb.exceptions.ResourceNotFoundException;
import com.gopi.airbnb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepo bookingRepo;
    private final InventoryRepo inventoryRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final GuestService guestService;
    private final ReservationRepo reservationRepo;
    private final ReservationGuestRepo reservationGuestRepo;




    @Override
    public BookingResponse startBooking(BookingRequest bookingRequest) {
        LocalDate checkIn = LocalDate.parse(bookingRequest.check_in(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate checkOut = LocalDate.parse(bookingRequest.check_out(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if (checkIn.isBefore(LocalDate.now())) throw new InvalidDetailsException("CheckOut Date Cannot be PastDate");
        if (!checkIn.isBefore(checkOut)) throw new InvalidDetailsException("CheckIn and checkOut dates are invalid");

        List<Inventory> inventoryList = inventoryRepo.findByRoomIdAndDateGreaterThanEqualAndDateLessThan(bookingRequest.room_id(), checkIn, checkOut);

        int guestCount = bookingRequest.total_guest();
        long requiredDays = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (inventoryList.size() != requiredDays) return new BookingResponse(false);
        for (Inventory inventory : inventoryList) {
            int roomsRequired = (int) Math.ceil(guestCount / (double) inventory.getRoom().getCapacity());
            int availableRooms = inventory.getTotalCount() - inventory.getBookedCount();
            if (inventory.getClosed())return new BookingResponse(false);
            if (availableRooms < roomsRequired)return new BookingResponse(false);
        }
        return new BookingResponse(true);
    }

   public boolean checkAvailabilityAndCreateReservation(BookingRequest bookingRequest){//Todo need to send invetiry to start booking as argument. without call agin
        LocalDate checkIn = LocalDate.parse(bookingRequest.check_in(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate checkOut = LocalDate.parse(bookingRequest.check_out(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Inventory> inventoryList = inventoryRepo.findByRoomIdAndDateGreaterThanEqualAndDateLessThanForUpdate(bookingRequest.room_id(), checkIn, checkOut);
        System.out.println("Stated booking");
        int guestCount = bookingRequest.total_guest();
        BookingResponse bookingResponse = startBooking(bookingRequest);
        if(!bookingResponse.isBookingPossible())return false;
        for (Inventory inventory : inventoryList) {
            int roomsRequired = (int) Math.ceil(guestCount / (double) inventory.getRoom().getCapacity());
            System.out.println(roomsRequired+"--------------------");
            inventory.setHoldCount(inventory.getHoldCount()+roomsRequired);
            inventory.setTotalCount(inventory.getTotalCount()-roomsRequired);
        }
         return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveBookingResponse reserveBooking(BookingReserveRequest bookingReserveRequest) {
        User user = userRepo.findById(bookingReserveRequest.user_id()).orElseThrow(() -> new ResourceNotFoundException("User is not registered"));
        Room room = roomRepo.findById(bookingReserveRequest.room_id()).orElseThrow(() -> new ResourceNotFoundException("Room is not registered"));
        LocalDate checkIn = LocalDate.parse(bookingReserveRequest.check_in(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate checkOut = LocalDate.parse(bookingReserveRequest.check_out(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        boolean reservationDone= checkAvailabilityAndCreateReservation( new BookingRequest(
                bookingReserveRequest.hotel_id(),
                bookingReserveRequest.room_id(),
                bookingReserveRequest.user_id(),
                bookingReserveRequest.total_guest(),
                bookingReserveRequest.check_in(),
                bookingReserveRequest.check_out()));
        if (!reservationDone)
            return new ReserveBookingResponse(false,null);
        long requiredDays = ChronoUnit.DAYS.between(checkIn, checkOut);
        int roomsRequired = (int) Math.ceil(bookingReserveRequest.total_guest() / (double) room.getCapacity());
        Reservation reservation= Reservation.builder()
                .check_in(checkIn)
                .check_out(checkOut)
                .amount(room.getBasePrice().multiply(BigDecimal.valueOf(roomsRequired).multiply(BigDecimal.valueOf(requiredDays))))
                .paymentWindowStarted(LocalDateTime.now())
                .paymentWindowExpires(LocalDateTime.now().plusMinutes(1))
                .reservationStatus(ReservationStatus.ACTIVE)
                .totalGuest(bookingReserveRequest.total_guest())
                .user(user)
                .room(room)
                .build();
        Reservation savedReservation= reservationRepo.save(reservation);
                List<Guest> savedGuests = new ArrayList<>();
        for (GuestRequest guestRequest : bookingReserveRequest.guestDetails()) {
            Guest guest = Guest.builder()
                    .gender(Gender.valueOf(guestRequest.gender()))
                    .name(guestRequest.name())
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .build();
            Guest savedGuest = guestService.addGuest(guest);
            savedGuests.add(savedGuest);
        }
        for(Guest guest :savedGuests){
            ReservationGuest reservationGuest= ReservationGuest.builder()
                    .guest(guest)
                    .reservation(reservation)
                    .reservationStatus(ReservationStatus.ACTIVE)
                    .build();
            reservationGuestRepo.save(reservationGuest);
        }

        return new ReserveBookingResponse(true,reservation.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReservationAndInventory(){
           List<Reservation> expiredReservationList=reservationRepo.findByPaymentWindowExpiresBefore(LocalDateTime.now());
           for(Reservation reservation: expiredReservationList) {
               if (reservation.getReservationStatus() == ReservationStatus.ACTIVE) {
                   reservation.setReservationStatus(ReservationStatus.EXPIRED);
                   List<Inventory> expireInventory = inventoryRepo.findByRoomIdAndDateGreaterThanEqualAndDateLessThan(reservation.getRoom().getId(), reservation.getCheck_in(), reservation.getCheck_out());
                   for (Inventory inventory : expireInventory) {
                       int roomsRequired = (int) Math.ceil(reservation.getTotalGuest() / (double) inventory.getRoom().getCapacity());
                       inventory.setTotalCount(inventory.getTotalCount() + roomsRequired);
                       inventory.setHoldCount(inventory.getHoldCount() - roomsRequired);
                       inventoryRepo.save(inventory);
                   }
                  Reservation savedReservation= reservationRepo.save(reservation);
                   List<ReservationGuest> reservationGuestList= reservationGuestRepo.findByReservationId(savedReservation.getId());
                   for(ReservationGuest reservationGuest: reservationGuestList){
                       reservationGuest.setReservationStatus(ReservationStatus.EXPIRED);
                       reservationGuestRepo.save(reservationGuest);
                   }


               }
           }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateReservationAndInventoryByReservationStatus(Reservation reservation, boolean isReservationExpired) {

        List<Inventory> inventoryList = inventoryRepo.findByRoomIdAndDateGreaterThanEqualAndDateLessThanForUpdate(reservation.getRoom().getId(), reservation.getCheck_in(), reservation.getCheck_out());
        int guestCount = reservation.getTotalGuest();
        int roomsRequired = (int) Math.ceil(guestCount / (double) reservation.getRoom().getCapacity());
        for (Inventory inventory : inventoryList) {
            if (!isReservationExpired) {
                inventory.setHoldCount(inventory.getHoldCount() - roomsRequired);
                inventory.setBookedCount(inventory.getBookedCount() + roomsRequired);
                inventoryRepo.save(inventory);
            } else {
                inventory.setHoldCount(inventory.getTotalCount() - roomsRequired);
                inventory.setTotalCount(inventory.getBookedCount() + roomsRequired);
                inventoryRepo.save(inventory);
            }

        }

    }
    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepo.save(booking);
    }


}


