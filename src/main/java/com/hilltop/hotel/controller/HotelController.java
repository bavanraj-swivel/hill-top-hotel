package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.request.HotelRequestDto;
import com.hilltop.hotel.domain.response.HotelListResponseDto;
import com.hilltop.hotel.domain.response.ResponseWrapper;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.DataNotFoundExceptionHotel;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Hotel controller
 */
@RestController
@Slf4j
@RequestMapping("/api/hotel")
public class HotelController extends BaseController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * This method is used to add hotel details.
     *
     * @param hotelRequestDto hotel data.
     * @return success/error response.
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> addHotel(@RequestBody HotelRequestDto hotelRequestDto) {
        try {
            if (!hotelRequestDto.isRequiredFieldsAvailable()) {
                log.debug("Required fields missing. data: {}", hotelRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            hotelService.addHotel(hotelRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to add hotel. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to update hotel.
     *
     * @param hotelRequestDto hotelRequestDto
     * @return success/error response.
     */
    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateHotel(@RequestBody HotelRequestDto hotelRequestDto) {
        try {
            if (!hotelRequestDto.isRequiredFieldsAvailableForUpdate()) {
                log.debug("Required fields missing. data: {}", hotelRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            hotelService.updateHotel(hotelRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_UPDATED, null);
        } catch (DataNotFoundExceptionHotel e) {
            log.error("Data not found.", e);
            return getBadRequestErrorResponse(ErrorMessage.DATA_NOT_FOUND);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to update hotel. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to get all hotels.
     *
     * @return hotel list.
     */
    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<ResponseWrapper> listAllHotels(@PathVariable String searchTerm) {
        try {
            HotelListResponseDto hotelListResponseDto =
                    new HotelListResponseDto(hotelService.getHotelList(searchTerm));
            log.debug("Successfully returned all hotels.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_RETURNED, hotelListResponseDto);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to list all hotel data.", e);
            return getInternalServerError();
        }
    }
}
