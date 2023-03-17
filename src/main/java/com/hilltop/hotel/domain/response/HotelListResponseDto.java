package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.Hotel;
import lombok.Getter;

import java.util.List;
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
}
