package com.hilltop.hotel.enumeration;

import lombok.Getter;

/**
 * Error messages.
 */
@Getter
public enum ErrorMessage {

    INTERNAL_SERVER_ERROR("Something went wrong."),
    MISSING_REQUIRED_FIELDS("Required fields are missing."),
    ROOM_LIMIT_REACHED("Maximum room count limit reached for hotel."),
    DATA_NOT_FOUND("Data not found.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
