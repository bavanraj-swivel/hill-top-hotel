package com.hilltop.hotel.exception;

/**
 * HillTop hotel application exception
 */
public class HillTopHotelApplicationException extends RuntimeException {

    /**
     * Hill Top application exception with error message and throwable error.
     *
     * @param errorMessage error message
     * @param error        error
     */
    public HillTopHotelApplicationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

    /**
     * Hill Top application exception with error message.
     *
     * @param errorMessage error message
     */
    public HillTopHotelApplicationException(String errorMessage) {
        super(errorMessage);
    }
}
