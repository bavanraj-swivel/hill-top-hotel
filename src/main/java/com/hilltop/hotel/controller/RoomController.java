package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.UpdateRoomRequestDto;
import com.hilltop.hotel.domain.response.ResponseWrapper;
import com.hilltop.hotel.domain.response.RoomListResponseDto;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.DataNotFoundException;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/room")
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
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_ADDED, null, HttpStatus.CREATED);
        } catch (DataNotFoundException e) {
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
     * @param updateRoomRequestDto updateRoomRequestDto
     * @return success/error response.
     */
    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateRoom(@RequestBody UpdateRoomRequestDto updateRoomRequestDto) {
        try {
            if (!updateRoomRequestDto.isRequiredFieldsAvailableForUpdate()) {
                log.debug(MISSING_FIELDS, updateRoomRequestDto.toLogJson());
                return getBadRequestErrorResponse(ErrorMessage.MISSING_REQUIRED_FIELDS);
            }
            roomService.updateRoom(updateRoomRequestDto);
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_UPDATED, null, HttpStatus.OK);
        } catch (DataNotFoundException e) {
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
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_DELETED, null, HttpStatus.OK);
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
    @GetMapping("/hotel/{id}/list")
    public ResponseEntity<ResponseWrapper> listAllRoomsByHotelId(@PathVariable String id,
                                                                 @RequestParam(required = false) String searchTerm) {
        try {
            RoomListResponseDto roomListResponseDto =
                    new RoomListResponseDto(roomService.getRoomListByHotelIdAndSearchTerm(id, searchTerm));
            log.debug("Successfully returned all rooms.");
            return getSuccessResponse(SuccessMessage.SUCCESSFULLY_RETURNED, roomListResponseDto, HttpStatus.OK);
        } catch (HillTopHotelApplicationException e) {
            log.error("Failed to list all room data.", e);
            return getInternalServerError();
        }
    }
}
