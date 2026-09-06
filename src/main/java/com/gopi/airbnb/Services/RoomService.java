package com.gopi.airbnb.Services;

import com.gopi.airbnb.dto.requests.RoomAddRequest;
import com.gopi.airbnb.dto.response.RoomAddResponse;
import com.gopi.airbnb.dto.response.RoomFetchResponse;
import com.gopi.airbnb.entitys.Room;

import java.util.List;

public interface RoomService {

    RoomAddResponse addRoom(RoomAddRequest request);

    Room findByRoomId(Long roomId);

    List<RoomFetchResponse> findByHotelId(Long hotelId);

    RoomFetchResponse getByRoomId(Long roomId);
}
