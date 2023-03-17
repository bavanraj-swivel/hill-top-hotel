package com.hilltop.hotel.repository;

import com.hilltop.hotel.domain.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Room type repository
 */
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
}
