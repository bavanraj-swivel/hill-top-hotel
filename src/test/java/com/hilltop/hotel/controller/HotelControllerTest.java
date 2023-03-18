package com.hilltop.hotel.controller;

import com.hilltop.hotel.domain.request.HotelRequestDto;
import com.hilltop.hotel.enumeration.ErrorMessage;
import com.hilltop.hotel.enumeration.SuccessMessage;
import com.hilltop.hotel.exception.DataNotFoundExceptionHotel;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Hotel controller test
 * Unit tests for {@link  HotelController}
 */
class HotelControllerTest {

    private static final String FAILED = "Failed.";
    private final String ADD_HOTEL_URI = "/api/hotel";
    private final String UPDATE_HOTEL_URI = "/api/hotel";
    private final String LIST_ALL_HOTEL_URI = "/api/hotel/search/ALL";
    private final HotelRequestDto hotelRequestDto = getHotelRequestDto();
    @Mock
    private HotelService hotelService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        HotelController hotelController = new HotelController(hotelService);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    /**
     * Unit tests for addHotel() method.
     */
    @Test
    void Should_ReturnOk_When_AddHotelIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_HOTEL_URI)
                        .content(hotelRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_ADDED.getMessage()));
    }

    @Test
    void Should_ReturnBadRequest_When_MissingRequiredFields() throws Exception {
        HotelRequestDto requestDto = hotelRequestDto;
        requestDto.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_HOTEL_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingHotelIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException(FAILED))
                .when(hotelService).addHotel(any());
        mockMvc.perform(MockMvcRequestBuilders.post(ADD_HOTEL_URI)
                        .content(hotelRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for updateHotel() method.
     */
    @Test
    void Should_ReturnOk_When_UpdateHotelIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_HOTEL_URI)
                        .content(hotelRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_UPDATED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnBadRequest_When_UpdateHotelFieldsAreMissing() throws Exception {
        HotelRequestDto requestDto = hotelRequestDto;
        requestDto.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_HOTEL_URI)
                        .content(requestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnInternalServerError_When_UpdateHotelFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException(FAILED))
                .when(hotelService).updateHotel(any());
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_HOTEL_URI)
                        .content(hotelRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void Should_ReturnDataNotFoundResponse_When_UpdateHotelIsFailed() throws Exception {
        doThrow(new DataNotFoundExceptionHotel(FAILED))
                .when(hotelService).updateHotel(any());
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_HOTEL_URI)
                        .content(hotelRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorMessage.DATA_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Unit tests for listAllHotels() method.
     */
    @Test
    void Should_ReturnOk_When_ListAllHotelsIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LIST_ALL_HOTEL_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessMessage.SUCCESSFULLY_RETURNED.getMessage()));
    }

    @Test
    void Should_ReturnInternalServerError_When_ListingAllHotelIsFailedDueToInternalErrors() throws Exception {
        doThrow(new HillTopHotelApplicationException(FAILED))
                .when(hotelService).getHotelList(anyString());
        mockMvc.perform(MockMvcRequestBuilders.get(LIST_ALL_HOTEL_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * This method is used to mock hotelRequestDto.
     *
     * @return hotelRequestDto
     */
    private HotelRequestDto getHotelRequestDto() {
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setId("hid-123");
        hotelRequestDto.setName("Hotel");
        hotelRequestDto.setLocation("Colombo");
        hotelRequestDto.setRoomCount(10);
        return hotelRequestDto;
    }


}