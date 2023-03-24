package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.domain.response.ResponseWrapper;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/roomType")
public class RoomTypeController extends BaseController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    /**
     * This method is used to add room type.
     *
     * @param roomTypeRequestDto roomTypeRequestDto
     * @return success/error response.
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> addRoomType(@RequestBody RoomTypeRequestDto roomTypeRequestDto) {
        try {
            if (!roomTypeRequestDto.isRequiredFieldsAvailable()) {
                log.debug("Required fields missing. data: {}", roomTypeRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            roomTypeService.addRoomType(roomTypeRequestDto);
            log.debug("Successfully added room type.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null, HttpStatus.CREATED);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to add room type. ", e);
            return getInternalServerError();
        }
    }
}
