package com.hilltop.hotel.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Hotel requestDto
 */
@Getter
@Setter
public class HotelRequestDto implements RequestDto {

    private String name;
    private String location;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(name) && isNonEmpty(location);
    }

}
