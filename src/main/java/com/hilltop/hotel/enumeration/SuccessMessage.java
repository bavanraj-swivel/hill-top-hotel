package com.hilltop.hotel.enumeration;

import lombok.Getter;

/**
 * Success messages.
 */
@Getter
public enum SuccessMessage {

    SUCCESSFULLY_ADDED("Successfully added."),
    SUCCESSFULLY_UPDATED("Successfully updated."),
    SUCCESSFULLY_RETURNED("Successfully returned data."),
    SUCCESSFULLY_DELETED("Successfully deleted.");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
