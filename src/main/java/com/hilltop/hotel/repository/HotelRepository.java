package com.hilltop.hotel.repository;

import com.hilltop.hotel.domain.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Hotel repository
 */
public interface HotelRepository extends JpaRepository<Hotel, String> {
}
