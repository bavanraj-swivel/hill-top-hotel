package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.request.UpdateHotelRequestDto;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        hotelService.getHotelList();
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelList() {
        when(hotelRepository.findAll()).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.getHotelList());
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
     * Unit tests for getHotelsByLocationAndPaxCount() method.
     */

    @Test
    void Should_ReturnHotelAndRoomMap_When_GetHotelsByLocationAndPaxCountIsCalled() {
        Room room1 = new Room();
        room1.setRoomNo("R1");
        room1.setMaxPeople(1);

        Room room2 = new Room();
        room2.setRoomNo("R12");
        room2.setMaxPeople(2);

        Hotel hotel1 = getHotel();
        hotel1.setRooms(Set.of(room1, room2, getRoom()));
        when(hotelRepository.findByLocation(anyString())).thenReturn(List.of(hotel1));
        Map<Hotel, List<Room>> map = hotelService.getHotelsByLocationAndPaxCount(anyString(), 5);
        assertEquals(1, map.size());
    }

    @Test
    void Should_ReturnEmptyMap_When_NoPossibleCombinationIsFoundForGetHotelsByLocationAndPaxCount() {
        Room room1 = new Room();
        room1.setRoomNo("R1");
        room1.setMaxPeople(1);

        Room room2 = new Room();
        room2.setRoomNo("R12");
        room2.setMaxPeople(2);

        Hotel hotel1 = getHotel();
        hotel1.setRooms(Set.of(room1, room2, getRoom()));
        when(hotelRepository.findByLocation(anyString())).thenReturn(List.of(hotel1));
        Map<Hotel, List<Room>> map = hotelService.getHotelsByLocationAndPaxCount(anyString(), 10);
        assertEquals(0, map.size());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetHotelsByLocationAndPaxCount() {
        when(hotelRepository.findByLocation(anyString())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class,
                () -> hotelService.getHotelsByLocationAndPaxCount("Colombo", 2));
        assertEquals("Failed to get hotels from database.", exception.getMessage());
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

    /**
     * This method is used to mock hotel.
     *
     * @return hotel
     */
    private Hotel getHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel");
        hotel.setLocation("Colombo");
        hotel.setRooms(Set.of(getRoom()));
        return hotel;
    }

    /**
     * This method is used to mock room.
     *
     * @return room
     */
    private Room getRoom() {
        Room room = new Room();
        room.setRoomNo("R1");
        room.setMaxPeople(3);
        return room;
    }

}