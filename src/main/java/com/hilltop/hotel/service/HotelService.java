package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.request.HotelRequestDto;
import com.hilltop.hotel.domain.request.UpdateHotelRequestDto;
import com.hilltop.hotel.exception.DataNotFoundException;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Hotel service
 */
@Service
@Slf4j
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * This method is used to add hotel detail.
     *
     * @param hotelRequestDto hotelRequestDto
     */
    public void addHotel(HotelRequestDto hotelRequestDto) {
        try {
            hotelRepository.save(new Hotel(hotelRequestDto));
            log.debug("Successfully added hotel data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to save hotel info in database.", e);
        }
    }

    /**
     * This method is used to update hotel detail.
     *
     * @param updateHotelRequestDto updateHotelRequestDto
     */
    public void updateHotel(UpdateHotelRequestDto updateHotelRequestDto) {
        try {
            Hotel hotel = getHotelById(updateHotelRequestDto.getId());
            hotel.updateHotel(updateHotelRequestDto);
            hotelRepository.save(hotel);
            log.debug("Successfully updated hotel data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to update hotel info in database.", e);
        }
    }

    /**
     * This method is used to get hotel list.
     *
     * @param searchTerm ALL/search
     * @return hotel list.
     */
    public List<Hotel> getHotelList(String searchTerm) {
        try {
            if (searchTerm == null)
                return hotelRepository.findAll();
            return hotelRepository.findByNameContaining(searchTerm);
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get all hotel data from database.", e);
        }
    }

    /**
     * This method is used to get hotel detail by id.
     *
     * @param id hotelId
     * @return hotel detail.
     */
    public Hotel getHotelById(String id) {
        try {
            return hotelRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Hotel not found for id: " + id));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get hotel info from database.", e);
        }
    }
}
