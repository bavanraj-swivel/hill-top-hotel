package com.hilltop.hotel.repository;

import com.hilltop.hotel.domain.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Hotel repository
 */
public interface HotelRepository extends JpaRepository<Hotel, String> {

    /**
     * This method is used to find hotels by name.
     *
     * @param name hotel name
     * @return hotel list.
     */
    List<Hotel> findByNameContaining(String name);

    /**
     * This method is used to find hotels by location.
     *
     * @param location location
     * @return hotel list.
     */
    List<Hotel> findByLocation(String location);
}
