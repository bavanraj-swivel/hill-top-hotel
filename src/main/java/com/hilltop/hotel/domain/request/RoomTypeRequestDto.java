package com.hilltop.hotel.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Room type requestDto
 */
@Getter
@Setter
public class RoomTypeRequestDto implements RequestDto {

    private String name;
    private double baseAmount;
    private double amountPerPerson;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(name) && baseAmount > 0 && amountPerPerson > 0;
    }
}
