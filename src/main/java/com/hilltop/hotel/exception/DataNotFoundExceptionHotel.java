package com.hilltop.hotel.exception;

/**
 * Data not found exception
 */
public class DataNotFoundExceptionHotel extends HillTopHotelApplicationException {
    /**
     * Hill Top application exception with error message.
     *
     * @param errorMessage error message
     */
    public DataNotFoundExceptionHotel(String errorMessage) {
        super(errorMessage);
    }
}
