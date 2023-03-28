package com.hilltop.hotel.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Hotel responseDto
 */
@Getter
public class HotelResponseDto implements ResponseDto {

    private final String id;
    private final String name;
    private final String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RoomResponseDto> rooms;

    public HotelResponseDto(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.location = hotel.getLocation();
    }

    public HotelResponseDto(Hotel hotel, List<Room> roomList) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.location = hotel.getLocation();
        this.rooms = roomList.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }
}
