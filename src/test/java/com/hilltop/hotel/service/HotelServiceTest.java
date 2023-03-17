package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.request.HotelRequestDto;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Hotel service test
 * Unit tests for {@link  HotelService}
 */
class HotelServiceTest {

    private final HotelRequestDto hotelRequestDto = getHotelRequestDto();
    private final Hotel hotel = new Hotel(getHotelRequestDto());
    @Mock
    private HotelRepository hotelRepository;
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        hotelService = new HotelService(hotelRepository);
    }

    /**
     * Unit tests for addHotel() method.
     */
    @Test
    void Should_SaveHotelDetailOnDatabase_When_ValidDataIsGiven() {
        hotelService.addHotel(hotelRequestDto);
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToAddHotelData() {
        when(hotelRepository.save(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            hotelService.addHotel(hotelRequestDto);
        });
        assertEquals("Failed to save hotel info in database.", exception.getMessage());
    }

    /**
     * Unit tests for updateHotel() method.
     */
    @Test
    void Should_UpdateHotelDetailOnDatabase_When_ValidDataIsGiven() {
        when(hotelRepository.findById(any())).thenReturn(Optional.of(hotel));
        hotelService.updateHotel(hotelRequestDto);
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToUpdateHotelData() {
        when(hotelRepository.findById(any())).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            hotelService.updateHotel(hotelRequestDto);
        });
        assertEquals("Failed to update hotel info in database.", exception.getMessage());
    }

    /**
     * Unit tests for getHotelList() method
     */
    @Test
    void Should_RunFindQuery_When_GetHotelListIsCalled() {
        hotelService.getHotelList();
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelList() {
        when(hotelRepository.findAll()).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            hotelService.getHotelList();
        });
        assertEquals("Failed to get all hotel data from database.", exception.getMessage());
    }

    /**
     * Unit tests for getHotelById() method.
     */
    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelById() {
        when(hotelRepository.findById(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            hotelService.getHotelById("hid-123");
        });
        assertEquals("Failed to get hotel info from database.", exception.getMessage());
    }

    /**
     * This method is used to mock hotelRequestDto.
     *
     * @return hotelRequestDto
     */
    private HotelRequestDto getHotelRequestDto() {
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setName("Hotel");
        hotelRequestDto.setLocation("Colombo");
        hotelRequestDto.setRoomCount(10);
        return hotelRequestDto;
    }

}