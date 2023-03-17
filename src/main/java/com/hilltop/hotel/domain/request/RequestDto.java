package com.hilltop.hotel.domain.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;

/**
 * RequestDto
 */
public interface RequestDto {

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    boolean isRequiredFieldsAvailable();

    /**
     * This method checks the given field is non-empty.
     *
     * @param field field
     * @return true/ false
     */
    default boolean isNonEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }

    /**
     * This method converts object to json string.
     *
     * @return json string
     */
    default String toLogJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new HillTopHotelApplicationException("Object to json conversion was failed.", e);
        }
    }
}
