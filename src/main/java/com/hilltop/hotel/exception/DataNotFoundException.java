package com.hilltop.hotel.exception;

/**
 * Data not found exception
 */
public class DataNotFoundException extends HillTopHotelApplicationException {
    /**
     * Hill Top application exception with error message.
     *
     * @param errorMessage error message
     */
    public DataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
