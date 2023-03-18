package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomRequestDto;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.DataNotFoundExceptionHotel;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Room controller test
 * Unit tests for {@link  RoomController}
 */
class RoomControllerTest {

    private final String ADD_ROOM_URI = "/api/room";
    private final String UPDATE_ROOM_URI = "/api/room";
    private final String DELETE_ROOM_URI = "/api/room/ID";
    private final String LIST_ROOM_URI = "/api/room/hotel/ID/search/ALL";
    private final String ADD_ROOM_TYPE_URI = "/api/room/type";
    private final RoomRequestDto roomRequestDto = getRoomRequestDto();
    private final RoomTypeRequestDto roomTypeRequestDto = getRoomTypeRequestDto();
    private final RoomType roomType = new RoomType(getRoomTypeRequestDto());
    private final Room room = getRoom();
    @Mock
    private RoomService roomService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        RoomController roomController = new RoomController(roomService);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    /**
     * Unit tests for addRoom() method.
     */
    @Test
    void Should_ReturnOk_When_AddRoomIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_MissingRequiredFields() throws Exception {
        RoomRequestDto requestDto = roomRequestDto;
        requestDto.setHotelId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingRoomIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException("Failed."))
                .when(roomService).addRoom(any());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnDataNotFoundResponse_When_AddingRoomIsFailed() throws Exception {
        doThrow(new DataNotFoundExceptionHotel("Failed."))
                .when(roomService).addRoom(any());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.DATA_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for updateRoom() method.
     */
    @Test
    void Should_ReturnOk_When_UpdateRoomIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_UPDATED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnBadRequest_When_UpdateRoomFieldsAreMissing() throws Exception {
        RoomRequestDto requestDto = roomRequestDto;
        requestDto.setHotelId(null);
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_ROOM_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_UpdateRoomFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException("Failed."))
                .when(roomService).updateRoom(any());
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnDataNotFoundResponse_When_UpdateHotelIsFailed() throws Exception {
        doThrow(new DataNotFoundExceptionHotel("Failed."))
                .when(roomService).updateRoom(any());
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_ROOM_URI)
                        .content(roomRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.DATA_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for deleteRoom() method.
     */
    @Test
    void Should_ReturnOk_When_DeleteRoomIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_ROOM_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_DELETED.getMessage()));
    }

    @Test
    void Should_ReturnInternalServerError_When_DeleteRoomIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException("Failed."))
                .when(roomService).deleteRoomById(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_ROOM_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for listAllRoomsByHotelId() method.
     */
    @Test
    void Should_ReturnOk_When_ListAllRoomsByHotelIdIsSuccessful() throws Exception {
        List<Room> roomList = List.of(room);
        when(roomService.getRoomListByHotelId(anyString())).thenReturn(roomList);
        mockMvc.perform(MockMvcRequestBuilders.get(LIST_ROOM_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_RETURNED.getMessage()));
    }

    @Test
    void Should_ReturnInternalServerError_When_ListAllRoomsByHotelIdIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException("Failed."))
                .when(roomService).getRoomListByHotelId(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get(LIST_ROOM_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for addRoomType() method.
     */
    @Test
    void Should_ReturnOk_When_AddRoomTypeIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_TYPE_URI)
                        .content(roomTypeRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_AddRoomTypeHasMissingRequiredFields() throws Exception {
        RoomTypeRequestDto requestDto = roomTypeRequestDto;
        requestDto.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_TYPE_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingRoomTypeIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException("Failed."))
                .when(roomService).addRoomType(any());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_TYPE_URI)
                        .content(roomTypeRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
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

    /**
     * This method is used to mock room.
     *
     * @return room
     */
    private Room getRoom() {
        Room room = new Room();
        room.setRoomNo("R1");
        room.setMaxPeople(5);
        room.setRoomType(roomType);
        return room;
    }
}