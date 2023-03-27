package com.hilltop.hotel.domain.response;

import com.hilltop.hotel.domain.entity.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Room type response dto
 */
@Getter
@Setter
@NoArgsConstructor
public class RoomTypeResponseDto implements ResponseDto {

    private String id;
    private String name;
    private double markupPercentage;

    public RoomTypeResponseDto(RoomType roomType) {
        this.id = roomType.getId();
        this.name = roomType.getName();
        this.markupPercentage = roomType.getMarkupPercentage();
    }
}
