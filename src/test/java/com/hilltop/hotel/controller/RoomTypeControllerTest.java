package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.entity.RoomType;
import com.hilltop.hotel.domain.request.RoomTypeRequestDto;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Room type controller test
 * Unit tests for {@link  RoomTypeController}
 */
class RoomTypeControllerTest {

    private final String ADD_ROOM_TYPE_URI = "/api/v1/roomType";
    private final RoomTypeRequestDto roomTypeRequestDto = getRoomTypeRequestDto();
    @Mock
    private RoomTypeService roomTypeService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        RoomTypeController roomTypeController = new RoomTypeController(roomTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(roomTypeController).build();
    }

    /**
     * Unit tests for addRoomType() method.
     */
    @Test
    void Should_ReturnOk_When_AddRoomTypeIsSuccessful() throws Exception {
        when(roomTypeService.addRoomType(any())).thenReturn(getRoomType());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_TYPE_URI)
                        .content(roomTypeRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
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
                .when(roomTypeService).addRoomType(any());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_ROOM_TYPE_URI)
                        .content(roomTypeRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * This method is used to mock roomTypeRequestDto.
     *
     * @return roomTypeRequestDto
     */
    private RoomTypeRequestDto getRoomTypeRequestDto() {
        RoomTypeRequestDto roomTypeRequestDto = new RoomTypeRequestDto();
        roomTypeRequestDto.setName("Gold");
        roomTypeRequestDto.setMarkupPercentage(5);
        return roomTypeRequestDto;
    }

    /**
     * This method is used to mock room type.
     *
     * @return room type.
     */
    private RoomType getRoomType() {
        RoomType roomType = new RoomType();
        roomType.setId("rtid-123");
        roomType.setName("GOLD");
        roomType.setMarkupPercentage(5);
        return roomType;
    }
}