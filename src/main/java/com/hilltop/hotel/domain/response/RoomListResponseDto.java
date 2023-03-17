package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.Room;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Room list responseDto
 */
@Getter
public class RoomListResponseDto implements ResponseDto {

    private final List<RoomResponseDto> roomList;

    public RoomListResponseDto(List<Room> roomList) {
        this.roomList = roomList.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }
}
