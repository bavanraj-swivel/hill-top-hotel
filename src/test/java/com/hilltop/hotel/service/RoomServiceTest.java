package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.RoomRepository;
import com.hilltop.hotel.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Room service test
 * Unit tests for {@link  RoomService}
 */
class RoomServiceTest {

    private final RoomRequestDto roomRequestDto = getRoomRequestDto();
    private final Room room = getRoom();
    private final Hotel hotel = getHotel();
    private final RoomTypeRequestDto roomTypeRequestDto = getRoomTypeRequestDto();
    private final RoomType roomType = new RoomType(getRoomTypeRequestDto());
    private RoomService roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomTypeRepository roomTypeRepository;
    @Mock
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        roomService = new RoomService(roomRepository, roomTypeRepository, hotelService);
    }

    /**
     * Unit tests for addRoom() method.
     */
    @Test
    void Should_SaveRoomDetailOnDatabase_When_ValidDataIsGiven() {
        when(hotelService.getHotelById(anyString())).thenReturn(hotel);
        when(roomTypeRepository.findById(anyString())).thenReturn(Optional.of(roomType));
        roomService.addRoom(roomRequestDto);
        verify(roomRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToAddRoomData() {
        when(hotelService.getHotelById(anyString())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.addRoom(roomRequestDto);
        });
        assertEquals("Failed to save room details on database.", exception.getMessage());
    }

    /**
     * Unit tests for updateRoom() method.
     */
    @Test
    void Should_updateRoomDetailOnDatabase_When_ValidDataIsGiven() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(hotelService.getHotelById(anyString())).thenReturn(hotel);
        when(roomTypeRepository.findById(anyString())).thenReturn(Optional.of(roomType));
        roomService.updateRoom(roomRequestDto);
        verify(roomRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToUpdateRoomData() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(hotelService.getHotelById(anyString())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.updateRoom(roomRequestDto);
        });
        assertEquals("Failed to update room info in database.", exception.getMessage());
    }

    /**
     * Unit tests for deleteRoomById() method.
     */
    @Test
    void Should_DeleteRoomDetailFromDatabase_When_QueryIsValid() {
        roomService.deleteRoomById("rid-123");
        verify(roomRepository, times(1)).deleteById(anyString());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_DeletingRoomDataIsFailed() {
        doThrow(new DataAccessException("Failed") {
        }).when(roomRepository).deleteById(anyString());
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.deleteRoomById("rid-123");
        });
        assertEquals("Failed to delete room from database.", exception.getMessage());
    }

    /**
     * Unit tests for getRoomListByHotelId() method.
     */
    @Test
    void Should_RunFindQuery_When_GetRoomListByHotelIdIsCalled() {
        roomService.getRoomListByHotelId(anyString());
        verify(roomRepository, times(1)).findAllByHotelId(anyString());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetRoomList() {
        when(roomRepository.findAllByHotelId(anyString())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.getRoomListByHotelId(anyString());
        });
        assertEquals("Failed to get all room data from database.", exception.getMessage());
    }

    /**
     * Unit tests for getRoomById() method
     */
    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetRoomById() {
        when(roomRepository.findById(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.getRoomById(anyString());
        });
        assertEquals("Failed to get room info from database.", exception.getMessage());
    }

    /**
     * Unit tests for addRoomType() method.
     */
    @Test
    void Should_SaveRoomTypeDetailOnDatabase_When_ValidDataIsGiven() {
        roomService.addRoomType(roomTypeRequestDto);
        verify(roomTypeRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToAddRoomTypeData() {
        when(roomTypeRepository.save(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.addRoomType(roomTypeRequestDto);
        });
        assertEquals("Failed to save room type on database.", exception.getMessage());
    }

    /**
     * Unit tests for getRoomTypeById() method.
     */
    @Test
    void Should_ThrowHillTopHotelApplicationException_When_FailedToGetRoomTypeById() {
        when(roomTypeRepository.findById(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopHotelApplicationException exception = assertThrows(HillTopHotelApplicationException.class, () -> {
            roomService.getRoomTypeById(anyString());
        });
        assertEquals("Failed to get room type from database.", exception.getMessage());
    }

    /**
     * This method is used to mock roomRequestDto.
     *
     * @return roomRequestDto
     */
    private RoomRequestDto getRoomRequestDto() {
        RoomRequestDto roomRequestDto = new RoomRequestDto();
        roomRequestDto.setId("rid-123");
        roomRequestDto.setRoomNo("R1");
        roomRequestDto.setHotelId("hid-123");
        roomRequestDto.setRoomTypeId("rtid-123");
        roomRequestDto.setMaxPeople(5);
        return roomRequestDto;
    }

    /**
     * This method is used to mock room.
     *
     * @return room
     */
    private Room getRoom() {
        Room room = new Room();
        room.setRoomNo("R1");
        room.setMaxPeople(5);
        return room;
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
        hotel.setRoomCount(10);
        return hotel;
    }

    /**
     * This method is used to mock roomTypeRequestDto.
     *
     * @return roomTypeRequestDto
     */
    private RoomTypeRequestDto getRoomTypeRequestDto() {
        RoomTypeRequestDto roomTypeRequestDto = new RoomTypeRequestDto();
        roomTypeRequestDto.setName("Gold");
        roomTypeRequestDto.setBaseAmount(1000);
        roomTypeRequestDto.setAmountPerPerson(100);
        return roomTypeRequestDto;
    }

}