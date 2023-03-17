package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.domain.response.ResponseWrapper;
import com.hilltop.hotel.domain.response.RoomListResponseDto;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.DataNotFoundExceptionHotel;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/room")
public class RoomController extends BaseController {

    private static final String MISSING_FIELDS = "Required fields missing. data: {}";
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * This method is used to add room details.
     *
     * @param roomRequestDto room data.
     * @return success/error response.
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> addRoom(@RequestBody RoomRequestDto roomRequestDto) {
        try {
            if (!roomRequestDto.isRequiredFieldsAvailable()) {
                log.debug(MISSING_FIELDS, roomRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            roomService.addRoom(roomRequestDto);
            log.debug("Successfully added room info.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (DataNotFoundExceptionHotel e) {
            log.error("Data not found.", e);
            return getBadRequestErrorResponse(ErrorMessage.DATA_NOT_FOUND);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to add room details. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to update room details.
     *
     * @param roomRequestDto roomRequestDto
     * @return success/error response.
     */
    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateRoom(@RequestBody RoomRequestDto roomRequestDto) {
        try {
            if (!roomRequestDto.isRequiredFieldsAvailableForUpdate()) {
                log.debug(MISSING_FIELDS, roomRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            roomService.updateRoom(roomRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_UPDATED, null);
        } catch (DataNotFoundExceptionHotel e) {
            log.error("Data not found.", e);
            return getBadRequestErrorResponse(ErrorMessage.DATA_NOT_FOUND);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to update room. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to delete room details.
     *
     * @param id roomId
     * @return success/error response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> deleteRoom(@PathVariable String id) {
        try {
            roomService.deleteRoomById(id);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_DELETED, null);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to delete room. ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to list room details by hotel id.
     *
     * @param id hotelId
     * @return room list.
     */
    @GetMapping("/all/hotel/{id}")
    public ResponseEntity<ResponseWrapper> listAllRoomsByHotelId(@PathVariable String id) {
        try {
            RoomListResponseDto roomListResponseDto = new RoomListResponseDto(roomService.getRoomListByHotelId(id));
            log.debug("Successfully returned all rooms.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_RETURNED, roomListResponseDto);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to list all room data.", e);
            return getInternalServerError();
        }
    }

    /**
     * This method is used to add room type.
     *
     * @param roomTypeRequestDto roomTypeRequestDto
     * @return success/error response.
     */
    @PostMapping("/type")
    public ResponseEntity<ResponseWrapper> addRoomType(@RequestBody RoomTypeRequestDto roomTypeRequestDto) {
        try {
            if (!roomTypeRequestDto.isRequiredFieldsAvailable()) {
                log.debug(MISSING_FIELDS, roomTypeRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            roomService.addRoomType(roomTypeRequestDto);
            log.debug("Successfully added room type.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to add room type. ", e);
            return getInternalServerError();
        }
    }
}
