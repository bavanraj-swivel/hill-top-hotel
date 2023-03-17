package com.hilltop.hotel.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Hotel requestDto
 */
@Getter
@Setter
public class HotelRequestDto implements RequestDto {

    private String id;
    private String name;
    private String location;
    private int roomCount;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(name) && isNonEmpty(location) && roomCount > 0;
    }

    /**
     * Used to validate required fields on update.
     *
     * @return true/false
     */
    public boolean isRequiredFieldsAvailableForUpdate() {
        return isNonEmpty(id) && isRequiredFieldsAvailable();
    }
}
