package com.hilltop.hotel.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Room requestDto
 */
@Getter
@Setter
public class RoomRequestDto implements RequestDto {

    private String id;
    private String roomNo;
    private String hotelId;
    private String roomTypeId;
    private int maxPeople;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(hotelId) && isNonEmpty(roomNo) && isNonEmpty(roomTypeId) && maxPeople > 0;
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
