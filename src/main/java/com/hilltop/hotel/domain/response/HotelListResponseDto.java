package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hotel list responseDto
 */
@Getter
public class HotelListResponseDto implements ResponseDto {

    private final List<HotelResponseDto> hotelList;

    public HotelListResponseDto(List<Hotel> hotelList) {
        this.hotelList = hotelList.stream().map(HotelResponseDto::new).collect(Collectors.toList());
    }

    public HotelListResponseDto(Map<Hotel, List<Room>> hotelAndRoomsMap) {
        List<HotelResponseDto> hotelResponseDtoList = new ArrayList<>();
        for (Map.Entry<Hotel, List<Room>> mapEntry : hotelAndRoomsMap.entrySet()) {
            hotelResponseDtoList.add(new HotelResponseDto(mapEntry.getKey(), mapEntry.getValue()));
        }
        this.hotelList = hotelResponseDtoList;
    }
}
