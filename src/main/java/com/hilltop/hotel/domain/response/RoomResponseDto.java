package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.Room;
import lombok.Getter;

/**
 * Room responseDto
 */
@Getter
public class RoomResponseDto implements ResponseDto {

    private final String id;
    private final String roomNo;
    private final String roomType;
    private final int maxPeople;
    private final double price;

    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.roomNo = room.getRoomNo();
        this.roomType = room.getRoomType().getName();
        this.maxPeople = room.getMaxPeople();
        this.price = room.getPrice();
    }
}
