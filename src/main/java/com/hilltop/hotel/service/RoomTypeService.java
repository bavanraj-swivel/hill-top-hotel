package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.exception.DataNotFoundException;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.RoomTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Room type service
 */
@Service
@Slf4j
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * This method is used to add room type.
     *
     * @param roomTypeRequestDto roomTypeRequestDto
     */
    public void addRoomType(RoomTypeRequestDto roomTypeRequestDto) {
        try {
            roomTypeRepository.save(new RoomType(roomTypeRequestDto));
            log.debug("Successfully added room type.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to save room type on database.", e);
        }
    }

    /**
     * This method is used to get room type by id.
     *
     * @param id roomTypeId
     * @return roomType.
     */
    public RoomType getRoomTypeById(String id) {
        try {
            return roomTypeRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Room type not found for id: " + id));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get room type from database.", e);
        }
    }
}
