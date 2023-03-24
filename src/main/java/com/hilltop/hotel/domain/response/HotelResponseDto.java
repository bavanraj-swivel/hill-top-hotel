package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.Hotel;
import lombok.Getter;

/**
 * Hotel responseDto
 */
@Getter
public class HotelResponseDto implements ResponseDto {

    private final String id;
    private final String name;
    private final String location;

    public HotelResponseDto(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.location = hotel.getLocation();
    }
}
