package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.exception.DataNotFoundExceptionHotel;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.RoomRepository;
import com.hilltop.hotel.repository.RoomTypeRepository;
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
    private final RoomTypeRepository roomTypeRepository;
    private final HotelService hotelService;

    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository, HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelService = hotelService;
    }

    /**
     * This method is used to add room detail.
     *
     * @param roomRequestDto roomRequestDto
     */
    public void addRoom(RoomRequestDto roomRequestDto) {
        try {
            Hotel hotel = hotelService.getHotelById(roomRequestDto.getHotelId());
            RoomType roomType = getRoomTypeById(roomRequestDto.getRoomTypeId());
            roomRepository.save(new Room(roomRequestDto, hotel, roomType));
            log.debug("Successfully added room data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to save room details on database.", e);
        }
    }

    /**
     * This method is used to update room detail.
     *
     * @param roomRequestDto roomRequestDto
     */
    public void updateRoom(RoomRequestDto roomRequestDto) {
        try {
            Room room = getRoomById(roomRequestDto.getId());
            Hotel hotel = hotelService.getHotelById(roomRequestDto.getHotelId());
            RoomType roomType = getRoomTypeById(roomRequestDto.getRoomTypeId());
            room.updateRoom(roomRequestDto, hotel, roomType);
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
     * @param hotelId hotelId
     * @return room list.
     */
    public List<Room> getRoomListByHotelId(String hotelId) {
        try {
            return roomRepository.findAllByHotelId(hotelId);
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
                    .orElseThrow(() -> new DataNotFoundExceptionHotel("Room not found for roomId: " + roomId));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get room info from database.", e);
        }
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
                    .orElseThrow(() -> new DataNotFoundExceptionHotel("Room type not found for id: " + id));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get room type from database.", e);
        }
    }
}
