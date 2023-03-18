package com.hilltop.hotel.repository;

import com.hilltop.hotel.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Room repository
 */
public interface RoomRepository extends JpaRepository<Room, String> {

    /**
     * Used to find all rooms by hotelId.
     *
     * @param hotelId hotelId
     * @return room list.
     */
    List<Room> findAllByHotelId(String hotelId);

    /**
     * Used to find all rooms by hotelId & roomNo.
     *
     * @param hotelId hotelId
     * @param roomNo  roomNo
     * @return room list.
     */
    List<Room> findAllByHotelIdAndRoomNoContaining(String hotelId, String roomNo);

    /**
     * Used to count rooms by hotelId.
     *
     * @param hotelId hotelId
     * @return room count.
     */
    int countByHotelId(String hotelId);
}
