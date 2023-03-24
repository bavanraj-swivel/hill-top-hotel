package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.UpdateRoomRequestDto;
import com.hilltop.hotel.exception.DataNotFoundException;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Room service
 */
@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    private final RoomTypeService roomTypeService;

    public RoomService(RoomRepository roomRepository, HotelService hotelService, RoomTypeService roomTypeService) {
        this.roomRepository = roomRepository;
        this.hotelService = hotelService;
        this.roomTypeService = roomTypeService;
    }

    /**
     * This method is used to add room detail.
     *
     * @param roomRequestDto roomRequestDto
     */
    public void addRoom(RoomRequestDto roomRequestDto) {
        try {
            Hotel hotel = hotelService.getHotelById(roomRequestDto.getHotelId());
            RoomType roomType = roomTypeService.getRoomTypeById(roomRequestDto.getRoomTypeId());
            roomRepository.save(new Room(roomRequestDto, hotel, roomType));
            log.debug("Successfully added room data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to save room details on database.", e);
        }
    }

    /**
     * This method is used to update room detail.
     *
     * @param updateRoomRequestDto updateRoomRequestDto
     */
    public void updateRoom(UpdateRoomRequestDto updateRoomRequestDto) {
        try {
            Room room = getRoomById(updateRoomRequestDto.getId());
            Hotel hotel = hotelService.getHotelById(updateRoomRequestDto.getHotelId());
            RoomType roomType = roomTypeService.getRoomTypeById(updateRoomRequestDto.getRoomTypeId());
            room.updateRoom(updateRoomRequestDto, hotel, roomType);
            roomRepository.save(room);
            log.debug("Successfully updated room data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to update room info in database.", e);
        }
    }

    /**
     * This method is used to delete room detail.
     *
     * @param roomId roomId
     */
    public void deleteRoomById(String roomId) {
        try {
            roomRepository.deleteById(roomId);
            log.debug("Successfully deleted room.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to delete room from database.", e);
        }
    }

    /**
     * This method is used to get room list by hotelId.
     *
     * @param hotelId    hotelId
     * @param searchTerm searchTerm
     * @return room list.
     */
    public List<Room> getRoomListByHotelIdAndSearchTerm(String hotelId, String searchTerm) {
        try {
            if (searchTerm == null)
                return roomRepository.findAllByHotelId(hotelId);
            return roomRepository.findAllByHotelIdAndRoomNoContaining(hotelId, searchTerm);
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get all room data from database.", e);
        }
    }

    /**
     * This method is used to get room detail by roomId.
     *
     * @param roomId roomId
     * @return room details.
     */
    public Room getRoomById(String roomId) {
        try {
            return roomRepository.findById(roomId)
                    .orElseThrow(() -> new DataNotFoundException("Room not found for roomId: " + roomId));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get room info from database.", e);
        }
    }
}
