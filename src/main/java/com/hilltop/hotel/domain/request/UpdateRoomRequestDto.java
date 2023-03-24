package com.hilltop.hotel.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Update room requestDto
 */
@Getter
@Setter
public class UpdateRoomRequestDto extends RoomRequestDto {

    private String id;

    /**
     * Used to validate required fields on update.
     *
     * @return true/false
     */
    public boolean isRequiredFieldsAvailableForUpdate() {
        return isNonEmpty(id) && isRequiredFieldsAvailable();
    }
}
