package com.hilltop.hotel.exception;

/**
 * Limit exceeded exception
 */
public class LimitExceededException extends HillTopHotelApplicationException {
    /**
     * Hill Top application exception with error message.
     *
     * @param errorMessage error message
     */
    public LimitExceededException(String errorMessage) {
        super(errorMessage);
    }
}
