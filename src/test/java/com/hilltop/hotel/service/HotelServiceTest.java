package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.request.UpdateHotelRequestDto;
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

    private static final String ALL = "ALL";
    private static final String FAILED = "Failed.";
    private final UpdateHotelRequestDto updateHotelRequestDto = getUpdateHotelRequestDto();
    private final Hotel hotel = new Hotel(getUpdateHotelRequestDto());
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
        hotelService.addHotel(updateHotelRequestDto);
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToAddHotelData() {
        when(hotelRepository.save(any())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.addHotel(updateHotelRequestDto));
        assertEquals("Failed to save hotel info in database.", exception.getMessage());
    }

    /**
     * Unit tests for updateHotel() method.
     */
    @Test
    void Should_UpdateHotelDetailOnDatabase_When_ValidDataIsGiven() {
        when(hotelRepository.findById(any())).thenReturn(Optional.of(hotel));
        hotelService.updateHotel(updateHotelRequestDto);
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToUpdateHotelData() {
        when(hotelRepository.findById(any())).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.updateHotel(updateHotelRequestDto));
        assertEquals("Failed to update hotel info in database.", exception.getMessage());
    }

    /**
     * Unit tests for getHotelList() method
     */
    @Test
    void Should_RunFindAllQuery_When_GetHotelListIsCalled() {
        hotelService.getHotelList(ALL);
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void Should_RunFindByNameQuery_When_GetHotelListIsCalledWithSearchTerm() {
        hotelService.getHotelList(anyString());
        verify(hotelRepository, times(1)).findByNameContaining(anyString());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelList() {
        when(hotelRepository.findAll()).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.getHotelList(ALL));
        assertEquals("Failed to get all hotel data from database.", exception.getMessage());
    }

    /**
     * Unit tests for getHotelById() method.
     */
    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelById() {
        when(hotelRepository.findById(any())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.getHotelById("hid-123"));
        assertEquals("Failed to get hotel info from database.", exception.getMessage());
    }

    /**
     * This method is used to mock hotelRequestDto.
     *
     * @return updateHotelRequestDto
     */
    private UpdateHotelRequestDto getUpdateHotelRequestDto() {
        UpdateHotelRequestDto updateHotelRequestDto = new UpdateHotelRequestDto();
        updateHotelRequestDto.setName("Hotel");
        updateHotelRequestDto.setLocation("Colombo");
        return updateHotelRequestDto;
    }

}