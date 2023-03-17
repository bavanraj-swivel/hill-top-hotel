package com.hilltop.hotel.repository;

import com.hilltop.hotel.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Room repository
 */
public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findAllByHotelId(String hotelId);
}
